package com.paraskcd.spotlightsearch.sources.domain.model.hits

import com.paraskcd.spotlightsearch.sources.domain.model.DeviceSetting

data class DeviceSettingHit(val setting: DeviceSetting) : SearchHit
