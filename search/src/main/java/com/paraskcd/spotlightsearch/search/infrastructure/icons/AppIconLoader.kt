package com.paraskcd.spotlightsearch.search.infrastructure.icons

import android.content.Context
import android.content.pm.LauncherApps
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.LruCache
import androidx.core.graphics.drawable.toBitmap
import com.paraskcd.spotlightsearch.search.domain.model.AppIconChoice
import com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack.IconPackRenderer
import com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack.IconPackSource
import com.paraskcd.spotlightsearch.sources.infrastructure.apps.UserProfiles
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIconLoader @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val profiles: UserProfiles,
    private val iconPacks: IconPackSource
) {
    private val cache = LruCache<String, Bitmap>(CACHE_ENTRIES)
    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    fun cached(
        packageName: String,
        profile: Long?,
        tint: Int?,
        background: Int? = null,
        iconPack: String? = null,
        choice: AppIconChoice? = null
    ): Bitmap? = cache.get(key(packageName, profile, tint, background, iconPack, choice))

    suspend fun load(
        packageName: String,
        profile: Long?,
        tint: Int?,
        background: Int? = null,
        iconPack: String? = null,
        choice: AppIconChoice? = null
    ): Bitmap? {
        cached(packageName, profile, tint, background, iconPack, choice)?.let { return it }
        return withContext(Dispatchers.IO) {
            val bitmap = choice?.let { chosenIcon(it) }
                ?: iconPack?.let { runCatching { packIcon(it, packageName, profile) }.getOrNull() }
                ?: systemIcon(packageName, profile, tint, background)
                ?: return@withContext null
            bitmap.also { cache.put(key(packageName, profile, tint, background, iconPack, choice), it) }
        }
    }

    fun cachedPackIcon(iconPack: String, drawable: String): Bitmap? = cache.get(packIconKey(iconPack, drawable))

    suspend fun loadPackIcon(iconPack: String, drawable: String): Bitmap? {
        cachedPackIcon(iconPack, drawable)?.let { return it }
        return withContext(Dispatchers.IO) {
            chosenIcon(AppIconChoice(iconPack, drawable))?.also { cache.put(packIconKey(iconPack, drawable), it) }
        }
    }

    private fun chosenIcon(choice: AppIconChoice): Bitmap? =
        runCatching { iconPacks.named(choice.iconPack, choice.drawable)?.toBitmap(ICON_PX, ICON_PX) }.getOrNull()

    private fun systemIcon(packageName: String, profile: Long?, tint: Int?, background: Int?): Bitmap? {
        val icon = runCatching { rawIcon(packageName, profile) }.getOrNull() ?: return null
        return if (tint != null) ThemedIconRenderer.render(icon, tint, background) else icon.toBitmap(ICON_PX, ICON_PX)
    }

    private fun packIcon(iconPack: String, packageName: String, profile: Long?): Bitmap? {
        val user = profiles.userOf(profile) ?: return null
        val activity = launcherApps.getActivityList(packageName, user).firstOrNull() ?: return null
        iconPacks.icon(iconPack, activity.componentName)?.let { return it.toBitmap(ICON_PX, ICON_PX) }
        val decoration = iconPacks.decoration(iconPack) ?: return null
        return IconPackRenderer.render(activity.getIcon(0), decoration, packageName.hashCode(), ICON_PX)
    }

    private fun rawIcon(packageName: String, profile: Long?): Drawable? {
        if (profile == null) return context.packageManager.getApplicationIcon(packageName)
        val user = profiles.userOf(profile) ?: return null
        return launcherApps.getActivityList(packageName, user).firstOrNull()?.getIcon(0)
    }

    private fun key(
        packageName: String,
        profile: Long?,
        tint: Int?,
        background: Int?,
        iconPack: String?,
        choice: AppIconChoice?
    ): String {
        val pack = iconPack?.let { "#$it#${LocalDate.now().dayOfMonth}" }.orEmpty()
        val chosen = choice?.let { "#${packIconKey(it.iconPack, it.drawable)}" }.orEmpty()
        return "$packageName#${profile ?: -1}#${tint ?: 0}#${background ?: 0}$pack$chosen"
    }

    private fun packIconKey(iconPack: String, drawable: String) = "pack:$iconPack/$drawable"

    private companion object {
        const val CACHE_ENTRIES = 256
        const val ICON_PX = 128
    }
}
