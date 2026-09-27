package com.paraskcd.spotlightsearch.sources.infrastructure.translation

import com.google.mlkit.nl.translate.Translator

class TranslatorCache(private val capacity: Int) {
    private val entries = LinkedHashMap<String, Translator>(capacity, LOAD_FACTOR, true)

    @Synchronized
    fun get(source: String, target: String, create: () -> Translator): Translator {
        val key = "$source>$target"
        entries[key]?.let { return it }
        val translator = create()
        entries[key] = translator
        if (entries.size > capacity) {
            val eldest = entries.keys.first()
            entries.remove(eldest)?.close()
        }
        return translator
    }

    private companion object {
        const val LOAD_FACTOR = 0.75f
    }
}
