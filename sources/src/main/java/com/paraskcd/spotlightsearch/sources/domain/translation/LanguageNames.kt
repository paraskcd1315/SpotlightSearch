package com.paraskcd.spotlightsearch.sources.domain.translation

object LanguageNames {
    private val codesByName = mapOf(
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
    private val namesByCode = codesByName.entries.associate { (name, code) ->
        code to name.replaceFirstChar { it.uppercaseChar() }
    }

    fun code(nameOrCode: String): String = codesByName[nameOrCode.lowercase()] ?: nameOrCode.lowercase()

    fun name(code: String): String = namesByCode[code.lowercase()] ?: code
}
