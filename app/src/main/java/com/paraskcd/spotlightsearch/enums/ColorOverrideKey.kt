package com.paraskcd.spotlightsearch.enums
enum class ColorOverrideKey {
    surface,
    surfaceBright,
    background,
    surfaceTint,
    onSurface,
    outline;

    val title: String get() = when (this) {
        background -> "Background"
        surface -> "Background 2"
        surfaceBright -> "Container background"
        surfaceTint -> "Container tint"
        onSurface -> "Text color"
        outline -> "Border color"
    }
}