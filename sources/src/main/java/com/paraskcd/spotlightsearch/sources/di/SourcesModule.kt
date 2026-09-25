package com.paraskcd.spotlightsearch.sources.di

import com.paraskcd.spotlightsearch.sources.data.ContactsRepositoryImpl
import com.paraskcd.spotlightsearch.sources.data.DeviceSettingsRepositoryImpl
import com.paraskcd.spotlightsearch.sources.data.InstalledAppsRepositoryImpl
import com.paraskcd.spotlightsearch.sources.data.QuickSearchRepositoryImpl
import com.paraskcd.spotlightsearch.sources.data.SpellingRepositoryImpl
import com.paraskcd.spotlightsearch.sources.data.SuggestionsRepositoryImpl
import com.paraskcd.spotlightsearch.sources.data.TranslationRepositoryImpl
import com.paraskcd.spotlightsearch.sources.domain.repository.ActionRunner
import com.paraskcd.spotlightsearch.sources.domain.repository.ContactsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.DeviceSettingsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.QuickSearchRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.SpellingRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.SuggestionsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.TranslationRepository
import com.paraskcd.spotlightsearch.sources.infrastructure.launch.IntentActionRunner
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SourcesModule {
    @Binds abstract fun installedApps(impl: InstalledAppsRepositoryImpl): InstalledAppsRepository
    @Binds abstract fun contacts(impl: ContactsRepositoryImpl): ContactsRepository
    @Binds abstract fun suggestions(impl: SuggestionsRepositoryImpl): SuggestionsRepository
    @Binds abstract fun translation(impl: TranslationRepositoryImpl): TranslationRepository
    @Binds abstract fun spelling(impl: SpellingRepositoryImpl): SpellingRepository
    @Binds abstract fun quickSearch(impl: QuickSearchRepositoryImpl): QuickSearchRepository
    @Binds abstract fun deviceSettings(impl: DeviceSettingsRepositoryImpl): DeviceSettingsRepository
    @Binds abstract fun actionRunner(impl: IntentActionRunner): ActionRunner

    companion object {
        @Provides
        @Singleton
        fun okHttpClient(): OkHttpClient = OkHttpClient()
    }
}
