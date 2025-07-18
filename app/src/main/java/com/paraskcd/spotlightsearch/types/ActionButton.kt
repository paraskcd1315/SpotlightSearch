package com.paraskcd.spotlightsearch.types

data class ActionButton(
    val label: String,
    val onClick: () -> Unit
)