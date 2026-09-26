package com.paraskcd.spotlightsearch.sources.domain.model.actions

import com.paraskcd.spotlightsearch.sources.domain.model.DeviceSetting

data class OpenDeviceSetting(val setting: DeviceSetting) : HitAction
