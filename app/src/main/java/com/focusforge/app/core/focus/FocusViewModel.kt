package com.focusforge.app.core.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val repository: FocusSessionRepository
) : ViewModel() {
    val active: StateFlow<FocusSession?> = repository.observeActive()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun start(allowlist: Set<String>, policy: FocusPolicy) = viewModelScope.launch {
        require(policy.canEnforce()) {
            "Device Owner Lock Task is required for enforced focus mode"
        }
        policy.begin(allowlist)
        try {
            repository.start(allowlist)
        } catch (error: Throwable) {
            policy.end()
            throw error
        }
    }

    fun pause(session: FocusSession) = viewModelScope.launch {
        runCatching { repository.pause(session.id, session.revision) }
    }

    fun resume(session: FocusSession) = viewModelScope.launch {
        runCatching { repository.resume(session.id, session.revision) }
    }

    fun end(session: FocusSession, policy: FocusPolicy) = viewModelScope.launch {
        runCatching {
            val ended = repository.end(session.id, session.revision)
            policy.end()
            ended
        }
    }
}
