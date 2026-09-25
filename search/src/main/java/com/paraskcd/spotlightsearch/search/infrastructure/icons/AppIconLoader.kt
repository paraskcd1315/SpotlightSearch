package com.paraskcd.spotlightsearch.search.infrastructure.icons

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import androidx.core.graphics.drawable.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIconLoader @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val cache = LruCache<String, Bitmap>(CACHE_ENTRIES)

    fun cached(packageName: String, tint: Int?): Bitmap? = cache.get(key(packageName, tint))

    suspend fun load(packageName: String, tint: Int?): Bitmap? {
        cached(packageName, tint)?.let { return it }
        return withContext(Dispatchers.IO) {
            val icon = runCatching { context.packageManager.getApplicationIcon(packageName) }.getOrNull()
                ?: return@withContext null
            val bitmap = if (tint != null) ThemedIconRenderer.render(icon, tint) else icon.toBitmap(ICON_PX, ICON_PX)
            bitmap.also { cache.put(key(packageName, tint), it) }
        }
    }

    private fun key(packageName: String, tint: Int?) = "$packageName#${tint ?: 0}"

    private companion object {
        const val CACHE_ENTRIES = 256
        const val ICON_PX = 128
    }
}
