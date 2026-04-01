package com.arteria.game.data.profile

import androidx.room.withTransaction
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomProfileRepository(
    private val database: ProfileDatabase,
) : ProfileRepository {
    private val dao = database.profileDao()

    override fun observeProfiles(): Flow<List<ProfileRecord>> =
        dao.observeProfiles().map { list -> list.map(ProfileEntity::toRecord) }

    override suspend fun createProfile(displayName: String): Result<ProfileRecord> {
        val trimmed = displayName.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("Name cannot be empty."))
        }

        val existing = dao.getByDisplayName(trimmed)
        if (existing != null) {
            return Result.failure(IllegalArgumentException("That name is already taken."))
        }

        return runCatching {
            val now = System.currentTimeMillis()
            val created = ProfileEntity(
                id = UUID.randomUUID().toString(),
                displayName = trimmed,
                createdAt = now,
                lastPlayedAt = now,
                gameMode = "Standard",
                isActive = false,
            )
            dao.insert(created)
            created.toRecord()
        }
    }

    override suspend fun setActiveProfile(profileId: String, playedAt: Long): Result<Unit> = runCatching {
        database.withTransaction {
            dao.clearActive()
            val affectedRows = dao.setActive(profileId = profileId, playedAt = playedAt)
            check(affectedRows > 0) { "Profile not found." }
        }
    }

    override suspend fun getProfileById(profileId: String): ProfileRecord? =
        dao.getById(profileId)?.toRecord()

    override suspend fun getActiveProfile(): ProfileRecord? =
        dao.getActive()?.toRecord()

    override suspend fun updateDisplayName(profileId: String, displayName: String): Result<Unit> {
        val trimmed = displayName.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("Name cannot be empty."))
        }

        val existing = dao.getByDisplayName(trimmed)
        if (existing != null && existing.id != profileId) {
            return Result.failure(IllegalArgumentException("That name is already taken."))
        }

        return runCatching {
            val rows = dao.updateDisplayName(profileId = profileId, displayName = trimmed)
            check(rows > 0) { "Profile not found." }
        }
    }

    override suspend fun deleteProfile(profileId: String): Result<Unit> = runCatching {
        val rows = dao.deleteById(profileId)
        check(rows > 0) { "Profile not found." }
    }
}
