package com.paraskcd.spotlightsearch.peek

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Messenger
import com.paraskcd.spotlightsearch.search.infrastructure.peek.PeekChannel
import com.paraskcd.spotlightsearch.search.infrastructure.peek.PeekProtocol
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeekService : Service() {
    @Inject
    lateinit var channel: PeekChannel

    private val messenger by lazy {
        Messenger(PeekMessageHandler(packageManager, packageName + PeekProtocol.PERMISSION_SUFFIX, channel))
    }

    override fun onBind(intent: Intent): IBinder = messenger.binder
}
