package com.paraskcd.spotlightsearch.search.infrastructure.icons

import android.content.Context
import android.content.pm.LauncherApps
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.LruCache
import androidx.core.graphics.drawable.toBitmap
import com.paraskcd.spotlightsearch.sources.infrastructure.apps.UserProfiles
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIconLoader @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val profiles: UserProfiles
) {
    private val cache = LruCache<String, Bitmap>(CACHE_ENTRIES)
    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    fun cached(packageName: String, profile: Long?, tint: Int?, background: Int? = null): Bitmap? =
        cache.get(key(packageName, profile, tint, background))

    suspend fun load(packageName: String, profile: Long?, tint: Int?, background: Int? = null): Bitmap? {
        cached(packageName, profile, tint, background)?.let { return it }
        return withContext(Dispatchers.IO) {
            val icon = runCatching { rawIcon(packageName, profile) }.getOrNull() ?: return@withContext null
            val bitmap = if (tint != null) ThemedIconRenderer.render(icon, tint, background) else icon.toBitmap(ICON_PX, ICON_PX)
            bitmap.also { cache.put(key(packageName, profile, tint, background), it) }
        }
    }

    private fun rawIcon(packageName: String, profile: Long?): Drawable? {
        if (profile == null) return context.packageManager.getApplicationIcon(packageName)
        val user = profiles.userOf(profile) ?: return null
        return launcherApps.getActivityList(packageName, user).firstOrNull()?.getIcon(0)
    }

    private fun key(packageName: String, profile: Long?, tint: Int?, background: Int?) =
        "$packageName#${profile ?: -1}#${tint ?: 0}#${background ?: 0}"

    private companion object {
        const val CACHE_ENTRIES = 256
        const val ICON_PX = 128
    }
}
