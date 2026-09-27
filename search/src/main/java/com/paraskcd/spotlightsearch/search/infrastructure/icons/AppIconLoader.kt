package com.paraskcd.spotlightsearch.search.infrastructure.icons

import android.content.Context
import android.content.pm.LauncherApps
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.UserHandle
import android.util.LruCache
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
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

    fun cached(packageName: String, profile: Long?, tint: Int?): Bitmap? = cache.get(key(packageName, profile, tint))

    suspend fun load(packageName: String, profile: Long?, tint: Int?): Bitmap? {
        cached(packageName, profile, tint)?.let { return it }
        return withContext(Dispatchers.IO) {
            val user = profiles.userOf(profile) ?: return@withContext null
            val icon = runCatching { rawIcon(packageName, profile, user) }.getOrNull() ?: return@withContext null
            val bitmap = if (tint != null) ThemedIconRenderer.render(icon, tint) else icon.toBitmap(ICON_PX, ICON_PX)
            badged(bitmap, profile, user).also { cache.put(key(packageName, profile, tint), it) }
        }
    }

    private fun rawIcon(packageName: String, profile: Long?, user: UserHandle): Drawable? =
        if (profile == null) context.packageManager.getApplicationIcon(packageName)
        else launcherApps.getActivityList(packageName, user).firstOrNull()?.getIcon(0)

    private fun badged(bitmap: Bitmap, profile: Long?, user: UserHandle): Bitmap =
        if (profile == null) bitmap
        else context.packageManager
            .getUserBadgedDrawableForDensity(bitmap.toDrawable(context.resources), user, badgeBounds(), 0)
            .toBitmap(ICON_PX, ICON_PX)

    private fun badgeBounds() = Rect(BADGE_START, BADGE_START, BADGE_END, BADGE_END)

    private fun key(packageName: String, profile: Long?, tint: Int?) = "$packageName#${profile ?: -1}#${tint ?: 0}"

    private companion object {
        const val CACHE_ENTRIES = 256
        const val ICON_PX = 128
        const val BADGE_END = 108
        const val BADGE_START = 68
    }
}
