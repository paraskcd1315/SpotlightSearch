package com.paraskcd.spotlightsearch.sources.data

import android.content.Context
import com.paraskcd.spotlightsearch.sources.domain.matching.MatchTier
import com.paraskcd.spotlightsearch.sources.domain.matching.NameMatch
import com.paraskcd.spotlightsearch.sources.domain.matching.NameMatcher
import com.paraskcd.spotlightsearch.sources.domain.matching.foldForSearch
import com.paraskcd.spotlightsearch.sources.domain.model.InstalledApp
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.ports.BlacklistPort
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import com.paraskcd.spotlightsearch.sources.infrastructure.apps.AppAliases
import com.paraskcd.spotlightsearch.sources.infrastructure.apps.PackageAppsSource
import com.paraskcd.spotlightsearch.sources.infrastructure.apps.PackageChangeReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstalledAppsRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val source: PackageAppsSource,
    blacklist: BlacklistPort
) : InstalledAppsRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val installed = MutableStateFlow<List<InstalledApp>?>(null)
    private val started = AtomicBoolean(false)

    private val visible: Flow<List<InstalledApp>> =
        combine(installed.filterNotNull(), blacklist.blacklistedPackages()) { all, hidden ->
            all.filterNot { it.packageName in hidden }
        }

    override val apps: StateFlow<List<InstalledApp>> =
        visible.stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val allApps: StateFlow<List<InstalledApp>?> = installed

    init {
        PackageChangeReceiver { reload() }.register(context)
    }

    override fun warmUp() {
        if (started.compareAndSet(false, true)) reload()
    }

    override suspend fun search(query: String): List<AppHit> {
        val folded = query.trim().foldForSearch()
        if (folded.isEmpty()) return emptyList()
        warmUp()
        val apps = visible.first()
        return withContext(Dispatchers.Default) {
            apps.mapNotNull { app -> bestMatch(app, folded)?.let { AppHit(app.packageName, app.label, it.tier, it.ranges) } }
                .sortedWith(compareBy<AppHit> { it.tier }.thenBy { it.label.foldForSearch() })
        }
    }

    private fun bestMatch(app: InstalledApp, folded: String): NameMatch? {
        val byLabel = NameMatcher.match(app.label, folded)
        val byAlias = AppAliases.byPackage[app.packageName]
            ?.mapNotNull { NameMatcher.match(it, folded) }
            ?.minByOrNull { it.tier }
            ?.let { NameMatch(maxOf(it.tier, MatchTier.INITIALS), emptyList()) }
        return listOfNotNull(byLabel, byAlias).minByOrNull { it.tier }
    }

    private fun reload() {
        scope.launch { installed.value = source.load() }
    }
}
