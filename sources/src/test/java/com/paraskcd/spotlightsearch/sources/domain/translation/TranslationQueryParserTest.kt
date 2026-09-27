package com.paraskcd.spotlightsearch.sources.domain.translation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    fun readsTextInLanguage() {
        assertEquals(
            TranslationRequest(text = "hello", targetLanguage = "ja"),
            parser.parse("hello in Japanese")
        )
    }

    @Test
    fun readsLanguageColonText() {
        assertEquals(
            TranslationRequest(text = "hola amigo", targetLanguage = "es"),
            parser.parse("spanish: hola amigo")
        )
    }

    @Test
    fun acceptsLanguageCodesOnlyAfterTranslate() {
        assertEquals(
            TranslationRequest(text = "hello", targetLanguage = "es"),
            parser.parse("translate hello to es")
        )
        assertNull(parser.parse("hello to es"))
    }

    @Test
    fun leavesOrdinaryQueriesAlone() {
        listOf(
            "hi there", "no problem", "it works", "he said", "go to it",
            "french fries", "german shepherd", "welcome to india", "calc", "spanish restaurants"
        ).forEach { assertNull(it, parser.parse(it)) }
    }

    @Test
    fun rejectsUnknownLanguagesEvenAfterTranslate() {
        assertNull(parser.parse("translate hello to klingon"))
    }
}
