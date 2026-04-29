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
        putAll(FarmingData.actionRegistry)
        putAll(ThievingData.actionRegistry)
        putAll(WoodworkingData.actionRegistry)
        putAll(TailoringData.actionRegistry)
        putAll(ForgingData.actionRegistry)
        putAll(JewelcraftingData.actionRegistry)
        putAll(FiremakingData.actionRegistry)
        putAll(RunecraftingData.actionRegistry)
        putAll(FletchingData.actionRegistry)
        putAll(AlchemyData.actionRegistry)
        putAll(ConstructionData.actionRegistry)
        putAll(TinkeringData.actionRegistry)
        putAll(EnchantingData.actionRegistry)
        putAll(DivinationData.actionRegistry)
        putAll(BardingData.actionRegistry)
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
        putAll(FarmingData.items)
        putAll(ThievingData.items)
        putAll(WoodworkingData.items)
        putAll(TailoringData.items)
        putAll(ForgingData.items)
        putAll(JewelcraftingData.items)
        putAll(FiremakingData.items)
        putAll(RunecraftingData.items)
        putAll(FletchingData.items)
        putAll(AlchemyData.items)
        putAll(ConstructionData.items)
        putAll(TinkeringData.items)
        putAll(EnchantingData.items)
        putAll(DivinationData.items)
        putAll(BardingData.items)
    }

    /** Actions available for a given skill, in display order. */
    fun actionsForSkill(skillId: SkillId): List<SkillAction> = when (skillId) {
        SkillId.MINING       -> MiningData.actions
        SkillId.LOGGING      -> LoggingData.actions
        SkillId.FISHING      -> FishingData.actions
        SkillId.HARVESTING   -> HarvestingData.actions
        SkillId.SCAVENGING   -> ScavengingData.actions
        SkillId.SMITHING     -> SmithingData.actions
        SkillId.HERBLORE     -> HerbloreData.actions
        SkillId.COOKING      -> CookingData.actions
        SkillId.FARMING      -> FarmingData.actions
        SkillId.THIEVING     -> ThievingData.actions
        SkillId.WOODWORKING  -> WoodworkingData.actions
        SkillId.TAILORING    -> TailoringData.actions
        SkillId.FORGING      -> ForgingData.actions
        SkillId.JEWELCRAFTING-> JewelcraftingData.actions
        SkillId.FIREMAKING   -> FiremakingData.actions
        SkillId.RUNECRAFTING -> RunecraftingData.actions
        SkillId.FLETCHING    -> FletchingData.actions
        SkillId.ALCHEMY      -> AlchemyData.actions
        SkillId.CONSTRUCTION -> ConstructionData.actions
        SkillId.TINKERING    -> TinkeringData.actions
        SkillId.ENCHANTING   -> EnchantingData.actions
        SkillId.DIVINATION   -> DivinationData.actions
        SkillId.BARDING      -> BardingData.actions
        else                 -> emptyList()
    }

    /** True when this skill has at least one trainable action in the registry. */
    fun isSkillImplemented(skillId: SkillId): Boolean = actionsForSkill(skillId).isNotEmpty()

    fun itemName(itemId: String): String =
        itemRegistry[itemId]?.name
            ?: itemId.replace("_", " ").replaceFirstChar { it.uppercase() }
}
