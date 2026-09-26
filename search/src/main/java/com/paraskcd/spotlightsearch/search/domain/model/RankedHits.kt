package com.paraskcd.spotlightsearch.search.domain.model

import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit

data class RankedHits(val top: SearchHit?, val apps: List<AppHit>, val contacts: List<ContactHit>)
