package com.paraskcd.spotlightsearch.peek

import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.paraskcd.spotlightsearch.search.infrastructure.peek.PeekChannel
import com.paraskcd.spotlightsearch.search.infrastructure.peek.PeekProtocol

class PeekMessageHandler(
    private val packageManager: PackageManager,
    private val permission: String,
    private val channel: PeekChannel
) : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        if (!allowed(msg.sendingUid)) return
        when (msg.what) {
            PeekProtocol.REGISTER -> channel.register(msg.replyTo)
            PeekProtocol.PROGRESS -> channel.progress(
                value = msg.data.getFloat(PeekProtocol.KEY_PROGRESS),
                distancePx = msg.data.getFloat(PeekProtocol.KEY_DISTANCE)
            )
            PeekProtocol.COMMIT -> channel.commit()
            PeekProtocol.CANCEL -> channel.cancel()
        }
    }

    private fun allowed(uid: Int): Boolean =
        packageManager.getPackagesForUid(uid).orEmpty().any { name ->
            packageManager.checkPermission(permission, name) == PackageManager.PERMISSION_GRANTED
        }
}
