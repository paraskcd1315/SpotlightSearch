package com.paraskcd.spotlightsearch.preferences.di

import com.paraskcd.spotlightsearch.preferences.data.RoomSearchSettingsRepository
import com.paraskcd.spotlightsearch.preferences.data.RoomThemeRepository
import com.paraskcd.spotlightsearch.preferences.domain.repository.SearchSettingsRepository
import com.paraskcd.spotlightsearch.preferences.domain.repository.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun theme(impl: RoomThemeRepository): ThemeRepository
    @Binds abstract fun searchSettings(impl: RoomSearchSettingsRepository): SearchSettingsRepository
}
