package com.paraskcd.spotlightsearch.sources.infrastructure.apps

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Handler
import android.os.Looper
import android.os.UserHandle

class PackageChangeCallback(private val onChange: () -> Unit) : LauncherApps.Callback() {
    override fun onPackageRemoved(packageName: String, user: UserHandle) = onChange()

    override fun onPackageAdded(packageName: String, user: UserHandle) = onChange()

    override fun onPackageChanged(packageName: String, user: UserHandle) = onChange()

    override fun onPackagesAvailable(packageNames: Array<out String>, user: UserHandle, replacing: Boolean) = onChange()

    override fun onPackagesUnavailable(packageNames: Array<out String>, user: UserHandle, replacing: Boolean) = onChange()

    fun register(context: Context) {
        context.getSystemService(LauncherApps::class.java)
            .registerCallback(this, Handler(Looper.getMainLooper()))
    }
}
