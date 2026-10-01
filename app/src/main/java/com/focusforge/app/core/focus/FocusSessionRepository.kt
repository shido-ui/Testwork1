package com.focusforge.app.core.focus

import com.focusforge.app.data.local.FocusForgeDatabase
import com.focusforge.app.data.local.FocusSessionConverters
import com.focusforge.app.data.local.FocusSessionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

class FocusSessionRepository(
    private val db: FocusForgeDatabase,
    private val clock: FocusClock
) {
    private val mutex = Mutex()

    fun observeActive(): Flow<FocusSession?> =
        db.focusSessionDao().observeActive().map { it?.toDomain() }

    suspend fun start(allowlist: Set<String>): FocusSession = mutex.withLock {
        require(allowlist.isNotEmpty()) { "At least one allowed package is required" }
        require(db.focusSessionDao().getActive() == null) { "A focus session is already active" }

        val entity = FocusSessionEntity(
            id = UUID.randomUUID().toString(),
            startedAtEpochMs = clock.epochMs(),
            startedElapsedMs = clock.elapsedMs(),
            state = FocusState.RUNNING.name,
            allowlist = FocusSessionConverters.encodeAllowlist(allowlist)
        )
        db.focusSessionDao().upsert(entity)
        entity.toDomain()
    }

    suspend fun pause(id: String, expectedRevision: Long): FocusSession =
        transition(id, expectedRevision, FocusState.RUNNING, FocusState.PAUSED)

    suspend fun resume(id: String, expectedRevision: Long): FocusSession =
        transition(id, expectedRevision, FocusState.PAUSED, FocusState.RUNNING)

    suspend fun end(id: String, expectedRevision: Long): FocusSession = mutex.withLock {
        val current = requireCurrent(id, expectedRevision)
        require(current.state != FocusState.ENDED.name) { "Session already ended" }
        val updated = current.copy(
            state = FocusState.ENDED.name,
            accumulatedElapsedMs = accumulated(current, clock.elapsedMs()),
            endedAtEpochMs = clock.epochMs(),
            revision = current.revision + 1
        )
        db.focusSessionDao().upsert(updated)
        updated.toDomain()
    }

    private suspend fun transition(
        id: String,
        expectedRevision: Long,
        from: FocusState,
        to: FocusState
    ): FocusSession = mutex.withLock {
        val current = requireCurrent(id, expectedRevision)
        require(current.state == from.name) {
            "Invalid transition " + current.state + " -> " + to.name
        }
        val updated = current.copy(
            state = to.name,
            accumulatedElapsedMs = if (from == FocusState.RUNNING)
                accumulated(current, clock.elapsedMs())
            else current.accumulatedElapsedMs,
            revision = current.revision + 1
        )
        db.focusSessionDao().upsert(updated)
        updated.toDomain()
    }

    private suspend fun requireCurrent(id: String, revision: Long): FocusSessionEntity =
        db.focusSessionDao().get(id)?.also {
            require(it.revision == revision) { "Stale focus-session revision" }
        } ?: error("Focus session not found")

    private fun accumulated(entity: FocusSessionEntity, nowElapsed: Long): Long =
        entity.accumulatedElapsedMs +
            (nowElapsed - entity.startedElapsedMs).coerceAtLeast(0L)

    private fun FocusSessionEntity.toDomain() = FocusSession(
        id = id,
        state = FocusState.valueOf(state),
        startedElapsedMs = startedElapsedMs,
        accumulatedElapsedMs = accumulatedElapsedMs,
        allowlist = FocusSessionConverters.decodeAllowlist(allowlist),
        revision = revision
    )
}

interface FocusClock {
    fun epochMs(): Long
    fun elapsedMs(): Long
}
