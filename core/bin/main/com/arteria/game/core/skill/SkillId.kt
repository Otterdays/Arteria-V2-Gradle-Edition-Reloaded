package com.arteria.game.core.skill

enum class SkillPillar(val displayName: String) {
    GATHERING("Gathering"),
    CRAFTING("Crafting"),
    COMBAT("Combat"),
    SUPPORT("Support"),
    COSMIC("Cosmic"),
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
    FARMING("Farming", SkillPillar.GATHERING, "Plant seeds and harvest cultivated crops"),
    THIEVING("Thieving", SkillPillar.GATHERING, "Pickpocket NPCs and loot stalls for gold and goods"),

    // Crafting
    SMITHING("Smithing", SkillPillar.CRAFTING, "Smelt ores and forge metal equipment"),
    COOKING("Cooking", SkillPillar.CRAFTING, "Prepare food for combat and healing"),
    RUNECRAFTING("Runecrafting", SkillPillar.CRAFTING, "Imbue runes with magical energy"),
    HERBLORE("Herblore", SkillPillar.CRAFTING, "Brew potions from herbs and reagents"),
    FORGING("Forging", SkillPillar.CRAFTING, "Craft advanced weapons and armor"),
    FIREMAKING("Firemaking", SkillPillar.CRAFTING, "Burn logs for light, heat, and utility buffs"),
    WOODWORKING("Woodworking", SkillPillar.CRAFTING, "Shape logs into furniture, staves, and shields"),
    TAILORING("Tailoring", SkillPillar.CRAFTING, "Sew cloth and leather into bags and gear"),
    FLETCHING("Fletching", SkillPillar.CRAFTING, "Fletch arrows, shafts, and ranged equipment"),
    CONSTRUCTION("Construction", SkillPillar.CRAFTING, "Blueprints and builds for housing and guild halls"),
    ALCHEMY("Alchemy", SkillPillar.CRAFTING, "Volatile transmutations and advanced battle concoctions"),

    // Combat
    ATTACK("Attack", SkillPillar.COMBAT, "Melee accuracy and damage"),
    STRENGTH("Strength", SkillPillar.COMBAT, "Maximum melee hit power"),
    DEFENCE("Defence", SkillPillar.COMBAT, "Damage reduction and armor effectiveness"),
    HITPOINTS("Hitpoints", SkillPillar.COMBAT, "Maximum health pool"),
    RANGED("Ranged", SkillPillar.COMBAT, "Bows, thrown weapons, and ranged combat prowess"),
    CONSTITUTION("Constitution", SkillPillar.COMBAT, "Health regeneration and status resilience"),
    SORCERY("Sorcery", SkillPillar.COMBAT, "Offensive spellcasting fueled by runes and focus"),
    SLAYER("Slayer", SkillPillar.COMBAT, "Bounty tasks against specific foes for unique rewards"),

    // Support
    AGILITY("Agility", SkillPillar.SUPPORT, "Movement speed and action efficiency"),
    WIZARDRY("Wizardry", SkillPillar.SUPPORT, "Arcane knowledge and spell research"),
    EXPLORATION("Exploration", SkillPillar.SUPPORT, "Survey regions, chart the world, and discover secrets"),
    ASTROLOGY("Astrology", SkillPillar.SUPPORT, "Study constellations for passive stellar buffs"),
    SUMMONING("Summoning", SkillPillar.SUPPORT, "Craft familiars and pouches for combat and skilling synergy"),
    CLEANSING("Cleansing", SkillPillar.SUPPORT, "Purify cursed gear and world corruption"),
    BARTER("Barter", SkillPillar.SUPPORT, "Haggle, trade routes, and black-market access"),
    RESEARCH("Research", SkillPillar.SUPPORT, "Passive knowledge tree unlocks across skills"),
    LEADERSHIP("Leadership", SkillPillar.SUPPORT, "Companions, morale, and group efficiency"),
    RESONANCE("Resonance", SkillPillar.SUPPORT, "Build momentum and global haste from focused action"),

    // Cosmic
    CHAOS_THEORY("Chaos Theory", SkillPillar.COSMIC, "Embrace randomness and serendipitous outcomes"),
    AETHER_WEAVING("Aether Weaving", SkillPillar.COSMIC, "Reality bending for late-game legendary crafting"),
    VOID_WALKING("Void Walking", SkillPillar.COSMIC, "Traverse reality tears and shortcuts"),
    CELESTIAL_BINDING("Celestial Binding", SkillPillar.COSMIC, "Spirits, contracts, and celestial automation"),
    CHRONOMANCY("Chronomancy", SkillPillar.COSMIC, "Time tricks and offline efficiency mastery"),
    ;

    companion object {
        fun byPillar(pillar: SkillPillar): List<SkillId> =
            entries.filter { it.pillar == pillar }
    }
}
