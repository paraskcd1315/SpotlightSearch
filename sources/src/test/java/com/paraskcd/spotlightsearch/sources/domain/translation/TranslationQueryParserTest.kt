package com.paraskcd.spotlightsearch.sources.domain.translation

import org.junit.Assert.assertEquals
import org.junit.Test

class TranslationQueryParserTest {
    private val parser = TranslationQueryParser()

    @Test
    fun resolvesBothLanguageNamesToCodes() {
        assertEquals(
            TranslationRequest(text = "hello", targetLanguage = "es", sourceLanguage = "en"),
            parser.parse("translate from English to Spanish: hello")
        )
    }

    @Test
    fun readsTranslateToThenText() {
        assertEquals(
            TranslationRequest(text = "good morning", targetLanguage = "de"),
            parser.parse("translate to german: good morning")
        )
    }

    @Test
    fun readsTextToLanguage() {
        assertEquals(
            TranslationRequest(text = "thank you", targetLanguage = "fr"),
            parser.parse("thank you to french")
        )
    }

    @Test
    fun namesALanguageCode() {
        assertEquals("Spanish", LanguageNames.name("es"))
    }
}
