package com.paraskcd.spotlightsearch.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.paraskcd.spotlightsearch.baselineprofile.SpotlightJourney.openSettings
import com.paraskcd.spotlightsearch.baselineprofile.SpotlightJourney.typeQueries
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(
        packageName = TargetApp.packageName(),
        includeInStartupProfile = true
    ) {
        pressHome()
        startActivityAndWait()
        typeQueries()
        openSettings()
    }
}
