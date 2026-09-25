package com.paraskcd.spotlightsearch.sources.infrastructure.spelling

import android.content.Context
import com.darkrockstudios.symspell.fdic.loadFdicFile
import com.darkrockstudios.symspellkt.common.Verbosity
import com.darkrockstudios.symspellkt.impl.SymSpell
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SymSpellChecker @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend fun load(): SymSpell {
        val checker = SymSpell()
        checker.dictionary.loadFdicFile(context.assets.open(DICTIONARY).use { it.readBytes() })
        return checker
    }

    fun closest(checker: SymSpell, word: String): String? =
        checker.lookup(word, Verbosity.Closest, MAX_EDIT_DISTANCE).firstOrNull()?.term

    private companion object {
        const val DICTIONARY = "en-80k.fdic"
        const val MAX_EDIT_DISTANCE = 2.0
    }
}
