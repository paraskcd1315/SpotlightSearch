package com.paraskcd.spotlightsearch.providers

import android.content.Context
import com.darkrockstudios.symspell.fdic.loadFdicFile
import com.darkrockstudios.symspellkt.common.Verbosity
import com.darkrockstudios.symspellkt.impl.SymSpell
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpellCheckerProvider @Inject constructor(@ApplicationContext private val context: Context) {
    private var spellChecker: SymSpell? = null

    suspend fun init() = withContext(Dispatchers.IO) {
        if (spellChecker == null) {
            val checker = SymSpell()
            val unigramBytes = context.assets.open("en-80k.fdic").readBytes()
            checker.dictionary.loadFdicFile(unigramBytes)
            spellChecker = checker
        }
    }

    fun suggest(word: String): List<String> {
        val sc = spellChecker ?: return emptyList()
        return sc.lookup(word, Verbosity.Closest, 2.0).map { it.term }
    }
}