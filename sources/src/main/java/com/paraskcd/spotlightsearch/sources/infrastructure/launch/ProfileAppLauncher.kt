package com.paraskcd.spotlightsearch.sources.infrastructure.launch

import android.content.ComponentName
import android.content.Context
import android.content.pm.LauncherApps
import android.os.UserHandle
import android.util.Log
import com.paraskcd.spotlightsearch.sources.infrastructure.apps.UserProfiles
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfileAppLauncher @Inject constructor(
    @ApplicationContext context: Context,
    private val profiles: UserProfiles
) {
    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    fun launch(packageName: String, profile: Long?) =
        open(packageName, profile) { component, user -> launcherApps.startMainActivity(component, user, null, null) }

    fun openInfo(packageName: String, profile: Long?) =
        open(packageName, profile) { component, user -> launcherApps.startAppDetailsActivity(component, user, null, null) }

    private fun open(packageName: String, profile: Long?, start: (ComponentName, UserHandle) -> Unit) {
        runCatching {
            val user = profiles.userOf(profile) ?: error("No profile $profile")
            val component = launcherApps.getActivityList(packageName, user).firstOrNull()?.componentName
                ?: error("No launcher activity in $user")
            start(component, user)
        }.onFailure { Log.w(TAG, "Cannot open $packageName", it) }
    }

    private companion object {
        const val TAG = "ProfileAppLauncher"
    }
}
