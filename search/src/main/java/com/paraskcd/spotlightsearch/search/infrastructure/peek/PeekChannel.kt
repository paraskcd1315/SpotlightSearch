package com.paraskcd.spotlightsearch.search.infrastructure.peek

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

    fun progress(value: Float) {
        held.value = PeekState(PeekPhase.Dragging, value.coerceIn(0f, 1f))
    }

    fun commit() {
        held.value = PeekState(PeekPhase.Committed, held.value?.progress ?: 0f)
    }

    fun cancel() {
        held.value = PeekState(PeekPhase.Cancelled, held.value?.progress ?: 0f)
    }

    fun begin() {
        if (held.value?.phase != PeekPhase.Dragging) held.value = PeekState(PeekPhase.Dragging, 0f)
    }

    fun closed() {
        held.value = null
        val target = client ?: return
        try {
            target.send(Message.obtain(null, PeekProtocol.CLOSED))
        } catch (_: RemoteException) {
            client = null
        }
    }
}
