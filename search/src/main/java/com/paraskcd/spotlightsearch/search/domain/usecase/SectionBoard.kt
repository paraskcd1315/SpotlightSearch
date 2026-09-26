package com.paraskcd.spotlightsearch.search.domain.usecase

import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class SectionBoard(private val emit: suspend (List<SearchSection>) -> Unit) {
    private val mutex = Mutex()
    private val sections = mutableMapOf<SectionKind, List<SearchHit>>()
    private var holding = true

    suspend fun put(kind: SectionKind, hits: List<SearchHit>) =
        update(kind) { hits }

    suspend fun update(kind: SectionKind, transform: (List<SearchHit>?) -> List<SearchHit>?) = mutex.withLock {
        val next = transform(sections[kind])
        if (next.isNullOrEmpty()) sections.remove(kind) else sections[kind] = next
        if (!holding) emit(snapshot())
    }

    suspend fun release() = mutex.withLock {
        if (!holding) return@withLock
        holding = false
        emit(snapshot())
    }

    private fun snapshot(): List<SearchSection> =
        sections.entries.sortedBy { it.key.ordinal }.map { SearchSection(it.key, it.value) }
}
