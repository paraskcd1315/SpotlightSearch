package com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack

import org.xmlpull.v1.XmlPullParser

object AppFilterParser {
    private const val ITEM = "item"
    private const val CALENDAR = "calendar"
    private const val ICON_BACK = "iconback"
    private const val ICON_MASK = "iconmask"
    private const val ICON_UPON = "iconupon"
    private const val SCALE = "scale"
    private const val COMPONENT = "component"
    private const val DRAWABLE = "drawable"
    private const val PREFIX = "prefix"
    private const val FACTOR = "factor"
    private const val IMAGE = "img"
    private const val WRAPPER = "ComponentInfo"
    private val BRACES = charArrayOf('{', '}', ' ')

    fun parse(parser: XmlPullParser): IconPackMap {
        val items = mutableMapOf<String, String>()
        val calendars = mutableMapOf<String, String>()
        val backs = mutableListOf<String>()
        var mask: String? = null
        var upon: String? = null
        var scale = 1f
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                ITEM -> entry(parser, DRAWABLE)?.let { (component, drawable) -> items.putIfAbsent(component, drawable) }
                CALENDAR -> entry(parser, PREFIX)?.let { (component, prefix) -> calendars.putIfAbsent(component, prefix) }
                ICON_BACK -> backs += images(parser)
                ICON_MASK -> mask = images(parser).firstOrNull() ?: mask
                ICON_UPON -> upon = images(parser).firstOrNull() ?: upon
                SCALE -> scale = parser.getAttributeValue(null, FACTOR)?.toFloatOrNull() ?: scale
            }
        }
        return IconPackMap(items, calendars, backs, mask, upon, scale)
    }

    fun component(raw: String): String? {
        val cleaned = raw.trim().removePrefix(WRAPPER).trim(*BRACES)
        if (cleaned.startsWith(':')) return null
        val packageName = cleaned.substringBefore('/', "").takeIf { it.isNotEmpty() } ?: return null
        val activity = cleaned.substringAfter('/').takeIf { it.isNotEmpty() } ?: return null
        return if (activity.startsWith('.')) "$packageName/$packageName$activity" else "$packageName/$activity"
    }

    private fun entry(parser: XmlPullParser, valueAttribute: String): Pair<String, String>? {
        val component = parser.getAttributeValue(null, COMPONENT)?.let(::component) ?: return null
        val value = parser.getAttributeValue(null, valueAttribute)?.takeIf { it.isNotBlank() } ?: return null
        return component to value
    }

    private fun images(parser: XmlPullParser): List<String> =
        (0 until parser.attributeCount)
            .filter { parser.getAttributeName(it).startsWith(IMAGE) }
            .map { parser.getAttributeValue(it) }
            .filter { it.isNotBlank() }
}
