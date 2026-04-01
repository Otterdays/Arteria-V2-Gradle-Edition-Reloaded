package com.arteria.game.data.profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY isActive DESC, lastPlayedAt DESC, createdAt DESC")
    fun observeProfiles(): Flow<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE id = :profileId LIMIT 1")
    suspend fun getById(profileId: String): ProfileEntity?

    @Query("SELECT * FROM profiles WHERE isActive = 1 LIMIT 1")
    suspend fun getActive(): ProfileEntity?

    @Query("SELECT * FROM profiles WHERE lower(displayName) = lower(:displayName) LIMIT 1")
    suspend fun getByDisplayName(displayName: String): ProfileEntity?

    @Query("UPDATE profiles SET isActive = 0")
    suspend fun clearActive()

    @Query("UPDATE profiles SET isActive = 1, lastPlayedAt = :playedAt WHERE id = :profileId")
    suspend fun setActive(profileId: String, playedAt: Long): Int

    @Query("UPDATE profiles SET displayName = :displayName WHERE id = :profileId")
    suspend fun updateDisplayName(profileId: String, displayName: String): Int
}
