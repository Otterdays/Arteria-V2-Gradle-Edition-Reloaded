package com.arteria.game.data.game

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM skill_states WHERE profileId = :profileId")
    suspend fun getSkillStates(profileId: String): List<SkillStateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSkillStates(states: List<SkillStateEntity>)

    @Query("SELECT * FROM bank_items WHERE profileId = :profileId")
    suspend fun getBankItems(profileId: String): List<BankItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBankItems(items: List<BankItemEntity>)

    @Query("DELETE FROM bank_items WHERE profileId = :profileId")
    suspend fun clearBank(profileId: String)

    @Query("SELECT * FROM game_meta WHERE profileId = :profileId")
    suspend fun getGameMeta(profileId: String): GameMetaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGameMeta(meta: GameMetaEntity)

    @Query("DELETE FROM skill_states WHERE profileId = :profileId")
    suspend fun deleteSkillStatesForProfile(profileId: String)

    @Query("DELETE FROM game_meta WHERE profileId = :profileId")
    suspend fun deleteGameMetaForProfile(profileId: String)
}

