package com.paraskcd.spotlightsearch.search.infrastructure.peek

object PeekProtocol {
    const val ACTION = "com.paraskcd.spotlightsearch.action.PEEK"
    const val EXTRA_PEEK = "com.paraskcd.spotlightsearch.extra.PEEK"
    const val PERMISSION_SUFFIX = ".permission.PEEK"
    const val REGISTER = 1
    const val PROGRESS = 2
    const val COMMIT = 3
    const val CANCEL = 4
    const val CLOSED = 10
    const val SHOWN = 11
    const val KEY_PROGRESS = "progress"
    const val KEY_DISTANCE = "distance"
}
