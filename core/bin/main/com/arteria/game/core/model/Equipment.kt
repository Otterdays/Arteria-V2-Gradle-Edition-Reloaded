package com.arteria.game.core.model

import com.arteria.game.core.data.EquipmentRegistry
import com.arteria.game.core.skill.SkillId

data class EquipmentSlot(
    val id: String,
    val displayName: String,
    val description: String,
)

data class Equipment(
    val id: String,
    val name: String,
    val slot: String,
    val description: String,
    val icon: String,
    val skillBoosts: Map<SkillId, Double>,
    /** Combat modifiers applied when encounter starts. */
    val combatStats: EquipmentCombatStats = EquipmentCombatStats(),
    val globalXpMultiplier: Double = 1.0,
    val levelRequired: Int = 1,
    val value: Int = 0,
)

data class EquipmentCombatStats(
    val accuracy: Int = 0,
    val maxHit: Int = 0,
    val meleeDefence: Int = 0,
    /** Subtracted from base attack interval (lower = faster). */
    val attackSpeedBonusMs: Long = 0L,
)

data class EquippedGear(
    val weapon: String? = null,
    val head: String? = null,
    val armor: String? = null,
    val accessory: String? = null,
    val ring: String? = null,
    val ring2: String? = null,
    val tool: String? = null,
)

/** Stable order for XP/skill boosts (tick) and summaries. */
fun EquippedGear.allEquippedIdsForGameplay(): List<String> = listOfNotNull(
    weapon,
    head,
    armor,
    accessory,
    ring,
    ring2,
    tool,
)

/** Attack snapshot uses combat-bearing gear only (excludes gathering tools). */
fun EquippedGear.combatItemIds(): List<String> =
    listOfNotNull(weapon, head, armor, accessory, ring, ring2)

/**
 * Repairs legacy saves when item definitions moved between slot kinds (registry slot id wins).
 * Concatenates two ring-pocket columns when both hold ring-type gear.
 */
fun normalizeEquippedGearFromColumns(
    weapon: String?,
    head: String?,
    armor: String?,
    accessory: String?,
    ring: String?,
    ring2Col: String?,
    tool: String?,
): EquippedGear {
    val singles = mutableMapOf<String, String>()
    val ringPocket = mutableListOf<String>()
    fun intake(itemId: String?) {
        val id = itemId ?: return
        val def = EquipmentRegistry.getById(id) ?: return
        when (def.slot) {
            EquipmentSlots.RING.id -> ringPocket.add(id)
            else -> singles[def.slot] = id
        }
    }
    intake(weapon)
    intake(head)
    intake(armor)
    intake(accessory)
    intake(ring)
    intake(ring2Col)
    intake(tool)

    fun single(key: String): String? = singles[key]
    return EquippedGear(
        weapon = single(EquipmentSlots.WEAPON.id),
        head = single(EquipmentSlots.HEAD.id),
        armor = single(EquipmentSlots.ARMOR.id),
        accessory = single(EquipmentSlots.ACCESSORY.id),
        ring = ringPocket.getOrNull(0),
        ring2 = ringPocket.getOrNull(1),
        tool = single(EquipmentSlots.TOOL.id),
    )
}

object EquipmentSlots {
    val WEAPON = EquipmentSlot("weapon", "Weapon", "Increases combat effectiveness")
    /** Helmets, hoods, and crowns. */
    val HEAD = EquipmentSlot("head", "Head", "Helmets and head-slot bonuses")
    val TOOL = EquipmentSlot("tool", "Tool", "Boosts gathering and crafting speed")
    val ARMOR = EquipmentSlot("armor", "Armor", "Provides defensive bonuses")
    val ACCESSORY = EquipmentSlot("accessory", "Accessory", "Cloaks, amulets, and charms")
    /**
     * Item registry slot id for wearable rings; persisted pockets use [RING] id (first ring)
     * and [RING2] id (second ring column).
     */
    val RING = EquipmentSlot("ring", "Ring", "Finger jewelry — equip two rings")
    val RING2 = EquipmentSlot("ring2", "Ring (II)", "Second ring pocket")

    /** Unique item-definition slots (inventory headers / registry grouping). */
    val allItemKinds = listOf(WEAPON, HEAD, ARMOR, ACCESSORY, RING, TOOL)

    /** Paper doll: silhouette (combat-centric) then utility tool at bottom. */
    val paperDollOrder =
        listOf(HEAD, WEAPON, ARMOR, ACCESSORY, RING, RING2, TOOL)
}
