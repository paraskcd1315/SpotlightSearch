package com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack

import android.content.Context
import android.content.Intent
import com.paraskcd.spotlightsearch.search.domain.model.IconPack
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IconPackCatalog @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend fun installed(): List<IconPack> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        IconPackIntents.actions
            .flatMap { action -> packageManager.queryIntentActivities(Intent(action), 0) }
            .distinctBy { it.activityInfo.packageName }
            .map { IconPack(it.activityInfo.packageName, it.activityInfo.applicationInfo.loadLabel(packageManager).toString()) }
            .sortedBy { it.label.lowercase() }
    }
}
