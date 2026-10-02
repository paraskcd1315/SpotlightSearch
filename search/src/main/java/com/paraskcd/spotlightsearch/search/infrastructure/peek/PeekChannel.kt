package com.paraskcd.spotlightsearch.search.infrastructure.peek

import android.os.Bundle
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekPhase
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekState
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class PeekChannel @Inject constructor() {
    private val held = MutableStateFlow<PeekState?>(null)
    private var client: Messenger? = null

    val state: StateFlow<PeekState?> = held.asStateFlow()

    fun register(replyTo: Messenger?) {
        client = replyTo
    }

    fun progress(value: Float, distancePx: Float) {
        held.value = PeekState(PeekPhase.Dragging, value.coerceIn(0f, 1f), distancePx.coerceAtLeast(0f))
    }

    fun commit() {
        held.value = ended(PeekPhase.Committed)
    }

    fun cancel() {
        held.value = ended(PeekPhase.Cancelled)
    }

    private fun ended(phase: PeekPhase): PeekState = held.value?.copy(phase = phase) ?: PeekState(phase, 0f)

    fun begin() {
        if (held.value?.phase != PeekPhase.Dragging) held.value = PeekState(PeekPhase.Dragging, 0f)
    }

    fun shown(fraction: Float) {
        send(Message.obtain(null, PeekProtocol.SHOWN).apply {
            data = Bundle().apply { putFloat(PeekProtocol.KEY_PROGRESS, fraction.coerceIn(0f, 1f)) }
        })
    }

    fun closed() {
        held.value = null
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
