package com.paraskcd.spotlightsearch.baselineprofile

import androidx.test.platform.app.InstrumentationRegistry

object TargetApp {
    private const val ARGUMENT = "targetAppId"

    fun packageName(): String = InstrumentationRegistry.getArguments().getString(ARGUMENT)
        ?: error("targetAppId is missing; run through the baselineprofile Gradle tasks")
}
