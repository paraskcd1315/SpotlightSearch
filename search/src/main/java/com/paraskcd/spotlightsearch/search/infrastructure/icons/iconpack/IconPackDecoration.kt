package com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack

import android.graphics.drawable.Drawable

data class IconPackDecoration(
    val backs: List<Drawable>,
    val mask: Drawable?,
    val upon: Drawable?,
    val scale: Float
)
