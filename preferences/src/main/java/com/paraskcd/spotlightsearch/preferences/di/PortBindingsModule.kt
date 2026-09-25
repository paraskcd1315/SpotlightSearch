package com.paraskcd.spotlightsearch.preferences.di

import com.paraskcd.spotlightsearch.preferences.data.RoomBlacklistPort
import com.paraskcd.spotlightsearch.preferences.data.RoomQuickSearchOrderPort
import com.paraskcd.spotlightsearch.sources.domain.ports.BlacklistPort
import com.paraskcd.spotlightsearch.sources.domain.ports.QuickSearchOrderPort
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PortBindingsModule {
    @Binds abstract fun blacklist(impl: RoomBlacklistPort): BlacklistPort
    @Binds abstract fun quickSearchOrder(impl: RoomQuickSearchOrderPort): QuickSearchOrderPort
}
