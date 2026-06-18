package com.arteria.game.core.data

import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable

data class ItemUsageReference(
    val skillId: SkillId,
    val actionId: String,
    val actionName: String,
)

data class ItemUsageInfo(
    val itemId: String,
    val producedBy: List<ItemUsageReference>,
    val consumedBy: List<ItemUsageReference>,
)

/**
 * Reverse index from [SkillDataRegistry.actionRegistry] for bank discovery UX.
 */
object ItemUsageIndex {

    private val producedByItem: Map<String, List<ItemUsageReference>> = buildMap {
        SkillDataRegistry.actionRegistry.values.forEach { action ->
            action.resourceId?.let { itemId ->
                put(itemId, getOrDefault(itemId, emptyList()) + action.toRef())
            }
        }
    }

    private val consumedByItem: Map<String, List<ItemUsageReference>> = buildMap {
        SkillDataRegistry.actionRegistry.values.forEach { action ->
            action.inputItems.keys.forEach { itemId ->
                put(itemId, getOrDefault(itemId, emptyList()) + action.toRef())
            }
        }
    }

    fun info(itemId: String): ItemUsageInfo = ItemUsageInfo(
        itemId = itemId,
        producedBy = producedByItem[itemId].orEmpty(),
        consumedBy = consumedByItem[itemId].orEmpty(),
    )

    /** Item ids that are inputs to at least one action the player can start now. */
    fun craftableInputItemIds(
        bank: Map<String, Int>,
        skillLevels: Map<SkillId, Int>,
    ): Set<String> = SkillDataRegistry.actionRegistry.values
        .filter { action -> canStartAction(action, bank, skillLevels) }
        .flatMap { it.inputItems.keys }
        .toSet()

    fun canStartAction(
        action: SkillAction,
        bank: Map<String, Int>,
        skillLevels: Map<SkillId, Int>,
    ): Boolean {
        val level = skillLevels[action.skillId] ?: 1
        if (level < action.levelRequired) return false
        return action.inputItems.all { (id, qty) -> (bank[id] ?: 0) >= qty }
    }

    private fun SkillAction.toRef() = ItemUsageReference(
        skillId = skillId,
        actionId = id,
        actionName = name,
    )

    fun skillLevelsFromXp(skills: Map<SkillId, SkillState>): Map<SkillId, Int> =
        skills.mapValues { (_, s) -> XPTable.levelForXp(s.xp) }
}
