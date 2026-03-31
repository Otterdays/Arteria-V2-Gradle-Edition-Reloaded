package com.arteria.game.core.skill

enum class SkillPillar(val displayName: String) {
    GATHERING("Gathering"),
    CRAFTING("Crafting"),
    COMBAT("Combat"),
    SUPPORT("Support"),
}

enum class SkillId(
    val displayName: String,
    val pillar: SkillPillar,
    val description: String,
) {
    // Gathering
    MINING("Mining", SkillPillar.GATHERING, "Extract ores and gems from rock formations"),
    LOGGING("Logging", SkillPillar.GATHERING, "Fell trees and gather wood"),
    FISHING("Fishing", SkillPillar.GATHERING, "Catch fish from rivers, lakes, and oceans"),
    HARVESTING("Harvesting", SkillPillar.GATHERING, "Gather herbs, crops, and botanical resources"),
    SCAVENGING("Scavenging", SkillPillar.GATHERING, "Salvage useful materials from the world"),

    // Crafting
    SMITHING("Smithing", SkillPillar.CRAFTING, "Smelt ores and forge metal equipment"),
    COOKING("Cooking", SkillPillar.CRAFTING, "Prepare food for combat and healing"),
    RUNECRAFTING("Runecrafting", SkillPillar.CRAFTING, "Imbue runes with magical energy"),
    HERBLORE("Herblore", SkillPillar.CRAFTING, "Brew potions from herbs and reagents"),
    FORGING("Forging", SkillPillar.CRAFTING, "Craft advanced weapons and armor"),

    // Combat
    ATTACK("Attack", SkillPillar.COMBAT, "Melee accuracy and damage"),
    STRENGTH("Strength", SkillPillar.COMBAT, "Maximum melee hit power"),
    DEFENCE("Defence", SkillPillar.COMBAT, "Damage reduction and armor effectiveness"),
    HITPOINTS("Hitpoints", SkillPillar.COMBAT, "Maximum health pool"),

    // Support
    AGILITY("Agility", SkillPillar.SUPPORT, "Movement speed and action efficiency"),
    WIZARDRY("Wizardry", SkillPillar.SUPPORT, "Arcane knowledge and spell research"),
    ;

    companion object {
        fun byPillar(pillar: SkillPillar): List<SkillId> =
            entries.filter { it.pillar == pillar }
    }
}

