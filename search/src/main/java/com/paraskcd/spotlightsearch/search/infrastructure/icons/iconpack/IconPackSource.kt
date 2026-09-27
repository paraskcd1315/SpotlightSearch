package com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack

import android.content.ComponentName
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.Xml
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IconPackSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val loaded = ConcurrentHashMap<String, LoadedPack>()

    fun icon(pack: String, component: ComponentName): Drawable? {
        val loaded = load(pack) ?: return null
        val key = component.flattenToString()
        val today = loaded.map.calendars[key]?.let { prefix -> drawable(loaded, prefix + LocalDate.now().dayOfMonth) }
        return today ?: loaded.map.items[key]?.let { drawable(loaded, it) }
    }

    fun named(pack: String, name: String): Drawable? = load(pack)?.let { drawable(it, name) }

    fun iconNames(pack: String): List<String> {
        val loaded = load(pack) ?: return emptyList()
        return loaded.map.items.values
            .distinct()
            .filter { loaded.resources.getIdentifier(it, DRAWABLE_TYPE, pack) != 0 }
            .sorted()
    }

    fun decoration(pack: String): IconPackDecoration? {
        val loaded = load(pack) ?: return null
        val backs = loaded.map.backs.mapNotNull { drawable(loaded, it) }
        if (backs.isEmpty()) return null
        return IconPackDecoration(
            backs = backs,
            mask = loaded.map.mask?.let { drawable(loaded, it) },
            upon = loaded.map.upon?.let { drawable(loaded, it) },
            scale = loaded.map.scale
        )
    }

    private fun load(pack: String): LoadedPack? = loaded[pack] ?: runCatching {
        val resources = context.packageManager.getResourcesForApplication(pack)
        LoadedPack(pack, resources, readMap(pack, resources)).also { loaded[pack] = it }
    }.onFailure { Log.w(TAG, "Icon pack $pack is unreadable", it) }.getOrNull()

    private fun readMap(pack: String, resources: Resources): IconPackMap {
        val id = resources.getIdentifier(APPFILTER, XML_TYPE, pack)
        if (id != 0) return resources.getXml(id).use(AppFilterParser::parse)
        return resources.assets.open(APPFILTER_ASSET).use { stream ->
            val parser = Xml.newPullParser().apply { setInput(stream, null) }
            AppFilterParser.parse(parser)
        }
    }

    private fun drawable(loaded: LoadedPack, name: String): Drawable? {
        val id = loaded.resources.getIdentifier(name, DRAWABLE_TYPE, loaded.packageName)
        if (id == 0) return null
        return runCatching { loaded.resources.getDrawable(id, null) }.getOrNull()
    }

    private companion object {
        const val TAG = "IconPackSource"
        const val APPFILTER = "appfilter"
        const val APPFILTER_ASSET = "appfilter.xml"
        const val XML_TYPE = "xml"
        const val DRAWABLE_TYPE = "drawable"
    }
}
