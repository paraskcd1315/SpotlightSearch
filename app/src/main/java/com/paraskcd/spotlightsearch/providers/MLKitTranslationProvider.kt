package com.paraskcd.spotlightsearch.providers
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri

import android.content.Context
import android.util.Log
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.types.SearchResult
import com.paraskcd.spotlightsearch.types.TranslationRequest
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.paraskcd.spotlightsearch.icons.Translate
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

@Singleton
class MLKitTranslationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun translate(query: String): SearchResult? {
        val parsed = parseTranslationQuery(query) ?: return null
        val targetLangCode = resolveLanguageCode(parsed.to.lowercase()) ?: parsed.to.lowercase()
        val target = TranslateLanguage.fromLanguageTag(targetLangCode) ?: return null
        val identifier = LanguageIdentification.getClient()
        val detectedLangTag = try {
            parsed.from ?: identifier.identifyLanguage(parsed.text).await()
        } catch (e: Exception) {
            Log.e("MLKit", "Language detection failed", e)
            return null
        }
        val source = TranslateLanguage.fromLanguageTag(detectedLangTag) ?: return null

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(source)
            .setTargetLanguage(target)
            .build()

        val translator = Translation.getClient(options)
        try {
            Log.d("MLKit", "Downloading translation model...")
            translator.downloadModelIfNeeded().await()
            Log.d("MLKit", "Model download complete.")
        } catch (e: Exception) {
            Log.e("MLKit", "Model download failed", e)
            return null
        }

        return try {
            val result = translator.translate(parsed.text).await()
            SearchResult(
                title = result,
                subtitle = "Translation (${resolveLanguageName(source.lowercase()) ?: source} → ${resolveLanguageName(target.lowercase()) ?: target.lowercase()})",
                iconVector = Translate,
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, parsed.text)
                            putExtra("key_text_input", parsed.text)
                            putExtra("key_text_output", "")
                            putExtra("key_language_from", parsed.from?.lowercase() ?: "auto")
                            putExtra("key_language_to", parsed.to.lowercase())
                            putExtra("key_suggest_translation", "")
                            putExtra("key_from_floating_window", false)
                            component = ComponentName(
                                "com.google.android.apps.translate",
                                "com.google.android.apps.translate.TranslateActivity"
                            )
                            type = "text/plain"
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        val url = "https://translate.google.com/?sl=auto&tl=${parsed.to}&text=${
                            Uri.encode(parsed.text)
                        }&op=translate"
                        val fallbackIntent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(fallbackIntent)
                    }
                },
                searchResultType = SearchResultType.TRANSLATOR
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun parseTranslationQuery(raw: String): TranslationRequest? {
        val lower = raw.trim().lowercase()

        val patterns = listOf(
            Regex("""translate\s+from\s+(\w+)\s+to\s+(\w+)\s*[:\-]?\s*(.+)""", RegexOption.IGNORE_CASE),
            Regex("""translate\s+to\s+(\w+)\s*[:\-]?\s*(.+)""", RegexOption.IGNORE_CASE),
            Regex("""translate\s+(.+)\s+to\s+(\w+)""", RegexOption.IGNORE_CASE),
            Regex("""(.+)\s+to\s+(\w+)""", RegexOption.IGNORE_CASE),
            Regex("""(\w+)\s*[:\-]?\s+(.+)""", RegexOption.IGNORE_CASE),
            Regex("""(.+)\s+in\s+(\w+)""", RegexOption.IGNORE_CASE)
        )

        for (pattern in patterns) {
            val match = pattern.find(lower)
            if (match != null) {
                return when (pattern.pattern) {
                    patterns[0].pattern -> {
                        val from = match.groupValues[1]
                        val to = match.groupValues[2]
                        val text = match.groupValues[3]
                        TranslationRequest(from = from, to = to, text = text)
                    }
                    patterns[1].pattern -> {
                        val to = match.groupValues[1]
                        val text = match.groupValues[2]
                        TranslationRequest(to = to, text = text)
                    }
                    patterns[2].pattern -> {
                        val text = match.groupValues[1]
                        val to = match.groupValues[2]
                        TranslationRequest(to = to, text = text)
                    }
                    patterns[3].pattern -> {
                        val text = match.groupValues[1]
                        val to = match.groupValues[2]
                        TranslationRequest(to = to, text = text)
                    }
                    patterns[4].pattern -> {
                        val to = match.groupValues[1]
                        val text = match.groupValues[2]
                        TranslationRequest(to = to, text = text)
                    }
                    patterns[5].pattern -> {
                        val text = match.groupValues[1]
                        val to = match.groupValues[2]
                        TranslationRequest(to = to, text = text)
                    }
                    else -> null
                }
            }
        }

        return null
    }
}

private fun resolveLanguageCode(name: String): String? {
    val map = mapOf(
        "albanian" to "sq", "arabic" to "ar", "belarusian" to "be", "bulgarian" to "bg",
        "bengali" to "bn", "catalan" to "ca", "chinese" to "zh", "croatian" to "hr",
        "czech" to "cs", "danish" to "da", "dutch" to "nl", "english" to "en",
        "esperanto" to "eo", "estonian" to "et", "finnish" to "fi", "french" to "fr",
        "galician" to "gl", "georgian" to "ka", "german" to "de", "greek" to "el",
        "gujarati" to "gu", "haitian" to "ht", "hebrew" to "he", "hindi" to "hi",
        "hungarian" to "hu", "icelandic" to "is", "indonesian" to "id", "irish" to "ga",
        "italian" to "it", "japanese" to "ja", "kannada" to "kn", "korean" to "ko",
        "lithuanian" to "lt", "latvian" to "lv", "macedonian" to "mk", "marathi" to "mr",
        "malay" to "ms", "maltese" to "mt", "norwegian" to "no", "persian" to "fa",
        "polish" to "pl", "portuguese" to "pt", "romanian" to "ro", "russian" to "ru",
        "slovak" to "sk", "slovenian" to "sl", "spanish" to "es", "swedish" to "sv",
        "swahili" to "sw", "tagalog" to "tl", "tamil" to "ta", "telugu" to "te",
        "thai" to "th", "turkish" to "tr", "ukrainian" to "uk", "urdu" to "ur",
        "vietnamese" to "vi", "welsh" to "cy"
    )
    return map[name.lowercase()]
}

private fun resolveLanguageName(codeOrName: String): String? {
    val map = mapOf(
        "sq" to "Albanian", "ar" to "Arabic", "be" to "Belarusian", "bg" to "Bulgarian",
        "bn" to "Bengali", "ca" to "Catalan", "zh" to "Chinese", "hr" to "Croatian",
        "cs" to "Czech", "da" to "Danish", "nl" to "Dutch", "en" to "English",
        "eo" to "Esperanto", "et" to "Estonian", "fi" to "Finnish", "fr" to "French",
        "gl" to "Galician", "ka" to "Georgian", "de" to "German", "el" to "Greek",
        "gu" to "Gujarati", "ht" to "Haitian", "he" to "Hebrew", "hi" to "Hindi",
        "hu" to "Hungarian", "is" to "Icelandic", "id" to "Indonesian", "ga" to "Irish",
        "it" to "Italian", "ja" to "Japanese", "kn" to "Kannada", "ko" to "Korean",
        "lt" to "Lithuanian", "lv" to "Latvian", "mk" to "Macedonian", "mr" to "Marathi",
        "ms" to "Malay", "mt" to "Maltese", "no" to "Norwegian", "fa" to "Persian",
        "pl" to "Polish", "pt" to "Portuguese", "ro" to "Romanian", "ru" to "Russian",
        "sk" to "Slovak", "sl" to "Slovenian", "es" to "Spanish", "sv" to "Swedish",
        "sw" to "Swahili", "tl" to "Tagalog", "ta" to "Tamil", "te" to "Telugu",
        "th" to "Thai", "tr" to "Turkish", "uk" to "Ukrainian", "ur" to "Urdu",
        "vi" to "Vietnamese", "cy" to "Welsh"
    )
    return map[codeOrName.lowercase()]
}