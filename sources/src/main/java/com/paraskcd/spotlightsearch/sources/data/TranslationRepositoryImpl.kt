package com.paraskcd.spotlightsearch.sources.data

import com.paraskcd.spotlightsearch.sources.domain.model.hits.TranslationHit
import com.paraskcd.spotlightsearch.sources.domain.repository.TranslationRepository
import com.paraskcd.spotlightsearch.sources.domain.translation.TranslationQueryParser
import com.paraskcd.spotlightsearch.sources.infrastructure.translation.MlKitTranslator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationRepositoryImpl @Inject constructor(
    private val parser: TranslationQueryParser,
    private val translator: MlKitTranslator
) : TranslationRepository {
    override suspend fun translate(query: String): TranslationHit? = withContext(Dispatchers.IO) {
        val request = parser.parse(query) ?: return@withContext null
        translator.translate(request)
    }
}
