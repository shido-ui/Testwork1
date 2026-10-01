package com.focusforge.app.core.focus

class FocusSessionUseCase(
    private val repository: FocusSessionRepository,
    private val policy: FocusPolicy
) {
    suspend fun start(allowlist: Set<String>): FocusSession {
        val session = repository.start(allowlist)
        if (policy.canEnforce()) policy.begin(allowlist)
        return session
    }

    suspend fun pause(id: String, revision: Long): FocusSession =
        repository.pause(id, revision)

    suspend fun resume(id: String, revision: Long): FocusSession =
        repository.resume(id, revision)

    suspend fun end(id: String, revision: Long): FocusSession {
        val session = repository.end(id, revision)
        if (policy.canEnforce()) policy.end()
        return session
    }
}
