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

    fun start(allowlist: Set<String>) = viewModelScope.launch {
        runCatching { repository.start(allowlist) }
    }

    fun pause(session: FocusSession) = viewModelScope.launch {
        runCatching { repository.pause(session.id, session.revision) }
    }

    fun resume(session: FocusSession) = viewModelScope.launch {
        runCatching { repository.resume(session.id, session.revision) }
    }

    fun end(session: FocusSession) = viewModelScope.launch {
        runCatching { repository.end(session.id, session.revision) }
    }
}
