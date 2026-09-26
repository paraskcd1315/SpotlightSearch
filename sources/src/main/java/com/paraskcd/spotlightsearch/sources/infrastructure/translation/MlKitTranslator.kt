package com.paraskcd.spotlightsearch.sources.infrastructure.translation

import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.paraskcd.spotlightsearch.sources.domain.model.hits.TranslationHit
import com.paraskcd.spotlightsearch.sources.domain.translation.TranslationRequest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class MlKitTranslator @Inject constructor() {
    private val identifier by lazy { LanguageIdentification.getClient() }

    suspend fun translate(request: TranslationRequest): TranslationHit? =
        withTimeoutOrNull(TRANSLATE_TIMEOUT_MS) { translateNow(request) }

    private suspend fun translateNow(request: TranslationRequest): TranslationHit? {
        val target = TranslateLanguage.fromLanguageTag(request.targetLanguage) ?: return null
        val sourceTag = request.sourceLanguage ?: identify(request.text) ?: return null
        val source = TranslateLanguage.fromLanguageTag(sourceTag) ?: return null

        val translator = Translation.getClient(
            TranslatorOptions.Builder().setSourceLanguage(source).setTargetLanguage(target).build()
        )
        return try {
            translator.downloadModelIfNeeded().await()
            TranslationHit(
                translation = translator.translate(request.text).await(),
                text = request.text,
                sourceLanguage = source,
                targetLanguage = target
            )
        } catch (e: Exception) {
            Log.w(TAG, "Translation failed", e)
            null
        } finally {
            translator.close()
        }
    }

    private suspend fun identify(text: String): String? = try {
        identifier.identifyLanguage(text).await()
    } catch (e: Exception) {
        Log.w(TAG, "Language detection failed", e)
        null
    }

    private companion object {
        const val TAG = "MlKitTranslator"
        const val TRANSLATE_TIMEOUT_MS = 3_000L
    }
}
