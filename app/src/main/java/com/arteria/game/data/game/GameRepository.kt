package com.arteria.game.data.game

import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId

class GameRepository(
    private val dao: GameDao,
    private val runInTransaction: suspend (suspend () -> Unit) -> Unit = { block -> block() },
) {

    suspend fun loadGameState(profileId: String): GameState {
        val skillEntities = dao.getSkillStates(profileId)
        val bankEntities = dao.getBankItems(profileId)
        val meta = dao.getGameMeta(profileId)

        val loaded = skillEntities.mapNotNull { entity ->
            val skillId = runCatching { SkillId.valueOf(entity.skillId) }.getOrNull()
            skillId?.let {
                it to SkillState(
                    skillId = it,
                    xp = entity.xp,
                    isTraining = entity.isTraining,
                    currentActionId = entity.currentActionId,
                    actionProgressMs = entity.actionProgressMs,
                )
            }
        }.toMap()

        val skills = SkillId.entries.associateWith { skillId ->
            loaded[skillId] ?: SkillState(skillId = skillId)
        }

        val bank = bankEntities.associate { it.itemId to it.quantity }

        return GameState(
            profileId = profileId,
            skills = skills,
            bank = bank,
            lastSaveTimestamp = meta?.lastSaveTimestamp ?: System.currentTimeMillis(),
        )
    }

    suspend fun saveGameState(state: GameState) {
        val skillEntities = state.skills.map { (skillId, skill) ->
            SkillStateEntity(
                profileId = state.profileId,
                skillId = skillId.name,
                xp = skill.xp,
                isTraining = skill.isTraining,
                currentActionId = skill.currentActionId,
                actionProgressMs = skill.actionProgressMs,
            )
        }

        val bankEntities = state.bank
            .filter { (_, qty) -> qty > 0 }
            .map { (itemId, qty) ->
                BankItemEntity(
                    profileId = state.profileId,
                    itemId = itemId,
                    quantity = qty,
                )
            }

        runInTransaction {
            dao.upsertSkillStates(skillEntities)
            dao.clearBank(state.profileId)
            dao.upsertBankItems(bankEntities)
            dao.upsertGameMeta(
                GameMetaEntity(
                    profileId = state.profileId,
                    lastSaveTimestamp = System.currentTimeMillis(),
                ),
            )
        }
    }
}

