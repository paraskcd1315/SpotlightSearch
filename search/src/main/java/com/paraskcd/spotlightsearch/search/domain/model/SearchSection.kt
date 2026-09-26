package com.paraskcd.spotlightsearch.search.domain.model

import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit

data class SearchSection(val kind: SectionKind, val hits: List<SearchHit>)
