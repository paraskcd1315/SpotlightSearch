package com.paraskcd.spotlightsearch.sources.infrastructure.apps

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Process
import android.os.UserHandle
import android.os.UserManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfiles @Inject constructor(
    @ApplicationContext context: Context
) {
    private val userManager = context.getSystemService(UserManager::class.java)
    private val launcherApps = context.getSystemService(LauncherApps::class.java)
    private val own = Process.myUserHandle()

    fun all(): List<UserHandle> = launcherApps.profiles

    fun profileOf(user: UserHandle): Long? =
        if (user == own) null else userManager.getSerialNumberForUser(user)

    fun userOf(profile: Long?): UserHandle? =
        if (profile == null) own else userManager.getUserForSerialNumber(profile)
}
