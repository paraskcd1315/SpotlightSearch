package com.paraskcd.spotlightsearch.enums
enum class ColorOverrideKey {
    background,
    surfaceBright,
    surfaceTint,
    onSurface,
    outline;

    val title: String get() = when (this) {
        background -> "Background"
        surfaceBright -> "Container background"
        surfaceTint -> "Container tint"
        onSurface -> "Text color"
        outline -> "Border color"
    }
}