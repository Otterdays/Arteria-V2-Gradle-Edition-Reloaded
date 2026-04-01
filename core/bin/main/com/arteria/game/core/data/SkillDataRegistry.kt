package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

/**
 * Single source of truth for all skill actions and item definitions.
 * Add new skill data objects here as they are implemented.
 */
object SkillDataRegistry {

    /** All skill actions across every implemented skill, keyed by action id. */
    val actionRegistry: Map<String, SkillAction> = buildMap {
        putAll(MiningData.actionRegistry)
        putAll(LoggingData.actionRegistry)
        putAll(FishingData.actionRegistry)
        putAll(HarvestingData.actionRegistry)
        putAll(HerbloreData.actionRegistry)
        putAll(ScavengingData.actionRegistry)
        putAll(SmithingData.actionRegistry)
        putAll(CookingData.actionRegistry)
    }

    /** All known item definitions across every implemented skill, keyed by item id. */
    val itemRegistry: Map<String, ItemDef> = buildMap {
        putAll(MiningData.items)
        putAll(LoggingData.items)
        putAll(FishingData.items)
        putAll(HarvestingData.items)
        putAll(HerbloreData.items)
        putAll(ScavengingData.items)
        putAll(SmithingData.items)
        putAll(CookingData.items)
    }

    /** Actions available for a given skill, in display order. */
    fun actionsForSkill(skillId: SkillId): List<SkillAction> = when (skillId) {
        SkillId.MINING    -> MiningData.actions
        SkillId.LOGGING   -> LoggingData.actions
        SkillId.FISHING   -> FishingData.actions
        SkillId.HARVESTING -> HarvestingData.actions
        SkillId.SCAVENGING -> ScavengingData.actions
        SkillId.SMITHING  -> SmithingData.actions
        SkillId.HERBLORE  -> HerbloreData.actions
        SkillId.COOKING   -> CookingData.actions
        else              -> emptyList()
    }

    /** True when this skill has at least one trainable action in the registry. */
    fun isSkillImplemented(skillId: SkillId): Boolean = actionsForSkill(skillId).isNotEmpty()

    fun itemName(itemId: String): String =
        itemRegistry[itemId]?.name
            ?: itemId.replace("_", " ").replaceFirstChar { it.uppercase() }
}
