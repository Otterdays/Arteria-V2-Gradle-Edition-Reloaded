package com.arteria.game.data.profile

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfiles(): Flow<List<ProfileRecord>>

    suspend fun createProfile(displayName: String): Result<ProfileRecord>

    suspend fun setActiveProfile(profileId: String, playedAt: Long): Result<Unit>

    suspend fun getProfileById(profileId: String): ProfileRecord?

    suspend fun getActiveProfile(): ProfileRecord?

    /** Returns failure if validation fails, name taken by another profile, or profile missing. */
    suspend fun updateDisplayName(profileId: String, displayName: String): Result<Unit>
}
