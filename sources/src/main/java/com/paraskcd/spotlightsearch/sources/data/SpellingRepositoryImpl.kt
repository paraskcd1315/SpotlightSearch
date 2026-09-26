package com.paraskcd.spotlightsearch.sources.data

import com.darkrockstudios.symspellkt.impl.SymSpell
import com.paraskcd.spotlightsearch.sources.domain.repository.SpellingRepository
import com.paraskcd.spotlightsearch.sources.infrastructure.spelling.SymSpellChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpellingRepositoryImpl @Inject constructor(
    private val checker: SymSpellChecker
) : SpellingRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val started = AtomicBoolean(false)
    @Volatile private var dictionary: SymSpell? = null

    override fun warmUp() {
        if (started.compareAndSet(false, true)) {
            scope.launch { dictionary = checker.load() }
        }
    }

    override suspend fun correction(query: String): String? {
        warmUp()
        val loaded = dictionary ?: return null
        return withContext(Dispatchers.Default) {
            checker.closest(loaded, query)?.takeUnless { it.equals(query, ignoreCase = true) }
        }
    }
}
