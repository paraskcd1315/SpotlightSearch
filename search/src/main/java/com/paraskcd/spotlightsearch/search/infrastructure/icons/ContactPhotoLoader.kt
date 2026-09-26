package com.paraskcd.spotlightsearch.search.infrastructure.icons

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactPhotoLoader @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val cache = LruCache<String, Bitmap>(CACHE_ENTRIES)

    fun cached(photoUri: String): Bitmap? = cache.get(photoUri)

    suspend fun load(photoUri: String): Bitmap? {
        cached(photoUri)?.let { return it }
        return withContext(Dispatchers.IO) {
            runCatching {
                context.contentResolver.openInputStream(photoUri.toUri())?.use(BitmapFactory::decodeStream)
            }.getOrNull()?.also { cache.put(photoUri, it) }
        }
    }

    private companion object {
        const val CACHE_ENTRIES = 128
    }
}
