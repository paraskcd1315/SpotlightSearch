package com.paraskcd.spotlightsearch.sources.infrastructure.translation

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.paraskcd.spotlightsearch.sources.domain.model.TranslationStatus
import com.paraskcd.spotlightsearch.sources.domain.model.hits.TranslationHit
import com.paraskcd.spotlightsearch.sources.domain.translation.TranslationRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MlKitTranslator @Inject constructor(
    @ApplicationContext context: Context
) {
    private val identifier by lazy { LanguageIdentification.getClient() }
    private val models = RemoteModelManager.getInstance()
    private val connectivity = context.getSystemService(ConnectivityManager::class.java)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val downloading = ConcurrentHashMap.newKeySet<String>()
    private val translators = TranslatorCache(MAX_TRANSLATORS)
    private val wifiOnly = DownloadConditions.Builder().requireWifi().build()

    suspend fun translate(request: TranslationRequest): TranslationHit? =
        withTimeoutOrNull(TRANSLATE_TIMEOUT_MS) { translateNow(request) }

    private suspend fun translateNow(request: TranslationRequest): TranslationHit? {
        val target = TranslateLanguage.fromLanguageTag(request.targetLanguage) ?: return null
        val sourceTag = request.sourceLanguage ?: identify(request.text) ?: return null
        val source = TranslateLanguage.fromLanguageTag(sourceTag) ?: return null
        if (source == target) return null
        val hit = TranslationHit(translation = "", text = request.text, sourceLanguage = source, targetLanguage = target)
        val translator = translators.get(source, target) { build(source, target) }
        if (!downloaded(source) || !downloaded(target)) {
            if (connectivity.isActiveNetworkMetered) return hit.copy(status = TranslationStatus.NEEDS_WIFI)
            download("$source>$target", translator)
            return hit.copy(status = TranslationStatus.DOWNLOADING)
        }
        return try {
            hit.copy(translation = translator.translate(request.text).await())
        } catch (e: Exception) {
            Log.w(TAG, "Translation failed", e)
            null
        }
    }

    private fun build(source: String, target: String): Translator = Translation.getClient(
        TranslatorOptions.Builder().setSourceLanguage(source).setTargetLanguage(target).build()
    )

    private suspend fun downloaded(language: String): Boolean =
        runCatching { models.isModelDownloaded(TranslateRemoteModel.Builder(language).build()).await() }.getOrDefault(false)

    private fun download(pair: String, translator: Translator) {
        if (!downloading.add(pair)) return
        scope.launch {
            runCatching { translator.downloadModelIfNeeded(wifiOnly).await() }
                .onFailure { Log.w(TAG, "Model download failed for $pair", it) }
            downloading.remove(pair)
        }
    }

    private suspend fun identify(text: String): String? = try {
        identifier.identifyLanguage(text).await().takeIf { it != UNDETERMINED }
    } catch (e: Exception) {
        Log.w(TAG, "Language detection failed", e)
        null
    }

    private companion object {
        const val TAG = "MlKitTranslator"
        const val TRANSLATE_TIMEOUT_MS = 3_000L
        const val MAX_TRANSLATORS = 4
        const val UNDETERMINED = "und"
    }
}
