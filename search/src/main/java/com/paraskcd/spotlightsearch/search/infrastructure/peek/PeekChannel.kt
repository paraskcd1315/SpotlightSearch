package com.paraskcd.spotlightsearch.search.infrastructure.peek

import android.os.Bundle
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekPhase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class PeekChannel @Inject constructor() {
    private val held = MutableStateFlow<PeekPhase?>(null)
    private var client: Messenger? = null
    private var progress = 0f
    private var distancePx = 0f
    private var follower: ((Float, Float) -> Unit)? = null

    val phase: StateFlow<PeekPhase?> = held.asStateFlow()

    fun register(replyTo: Messenger?) {
        client = replyTo
    }

    fun follow(follower: (Float, Float) -> Unit) {
        this.follower = follower
        follower(progress, distancePx)
    }

    fun unfollow(follower: (Float, Float) -> Unit) {
        if (this.follower === follower) this.follower = null
    }

    fun progress(value: Float, distancePx: Float) {
        progress = value.coerceIn(0f, 1f)
        this.distancePx = distancePx.coerceAtLeast(0f)
        held.value = PeekPhase.Dragging
        follower?.invoke(progress, this.distancePx)
    }

    fun commit() {
        held.value = PeekPhase.Committed
    }

    fun cancel() {
        held.value = PeekPhase.Cancelled
    }

    fun begin() {
        if (held.value == null) held.value = PeekPhase.Dragging
    }

    fun shown(fraction: Float) {
        send(Message.obtain(null, PeekProtocol.SHOWN).apply {
            data = Bundle().apply { putFloat(PeekProtocol.KEY_PROGRESS, fraction.coerceIn(0f, 1f)) }
        })
    }

    fun closed() {
        held.value = null
        progress = 0f
        distancePx = 0f
        send(Message.obtain(null, PeekProtocol.CLOSED))
    }

    private fun send(message: Message) {
        val target = client ?: return
        try {
            target.send(message)
        } catch (_: RemoteException) {
            client = null
        }
    }
}
