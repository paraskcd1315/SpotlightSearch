package com.paraskcd.spotlightsearch.preferences.infrastructure.system

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object ExternalLinks {
    fun open(context: Context, url: String): Boolean = try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
}
