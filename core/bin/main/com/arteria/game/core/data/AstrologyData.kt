package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Astrology turns celestial artifacts into advanced star buffs.
object AstrologyData {
    val STAR_READING = ItemDef("star_reading", "Star Reading", "A basic celestial reading")
    val CONSTELLATION_CHART = ItemDef(
        "constellation_chart",
        "Constellation Chart",
        "Tracked movement of major constellations",
    )
    val LUNAR_INSIGHT = ItemDef("lunar_insight", "Lunar Insight", "Moon-phase derived buff insight")
    val SOLAR_INSIGHT = ItemDef("solar_insight", "Solar Insight", "Sun-cycle derived power insight")
    val COSMIC_ALIGNMENT = ItemDef("cosmic_alignment", "Cosmic Alignment", "A stabilized celestial alignment")
    val CELESTIAL_FORECAST = ItemDef("celestial_forecast", "Celestial Forecast", "Predictive star cycle report")
    val ORBITAL_MATRIX = ItemDef("orbital_matrix", "Orbital Matrix", "High-order orbital tuning matrix")
    val STELLAR_EDICT = ItemDef("stellar_edict", "Stellar Edict", "A supreme decree drawn from the heavens")

    val items: Map<String, ItemDef> = listOf(
        STAR_READING,
        CONSTELLATION_CHART,
        LUNAR_INSIGHT,
        SOLAR_INSIGHT,
        COSMIC_ALIGNMENT,
        CELESTIAL_FORECAST,
        ORBITAL_MATRIX,
        STELLAR_EDICT,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "read_night_sky",
            skillId = SkillId.ASTROLOGY,
            name = "Read Night Sky",
            levelRequired = 1,
            xpPerAction = 20.0,
            actionTimeMs = 3000L,
            resourceId = "star_reading",
            inputItems = mapOf("starfruit" to 1),
        ),
        SkillAction(
            id = "chart_constellations",
            skillId = SkillId.ASTROLOGY,
            name = "Chart Constellations",
            levelRequired = 14,
            xpPerAction = 36.0,
            actionTimeMs = 3800L,
            resourceId = "constellation_chart",
            inputItems = mapOf("star_reading" to 1, "region_map" to 1),
        ),
        SkillAction(
            id = "derive_lunar_insight",
            skillId = SkillId.ASTROLOGY,
            name = "Derive Lunar Insight",
            levelRequired = 28,
            xpPerAction = 58.0,
            actionTimeMs = 4700L,
            resourceId = "lunar_insight",
            inputItems = mapOf("constellation_chart" to 1, "water_rune" to 6),
        ),
        SkillAction(
            id = "derive_solar_insight",
            skillId = SkillId.ASTROLOGY,
            name = "Derive Solar Insight",
            levelRequired = 42,
            xpPerAction = 88.0,
            actionTimeMs = 5800L,
            resourceId = "solar_insight",
            inputItems = mapOf("constellation_chart" to 1, "fire_rune" to 6),
        ),
        SkillAction(
            id = "stabilize_cosmic_alignment",
            skillId = SkillId.ASTROLOGY,
            name = "Stabilize Cosmic Alignment",
            levelRequired = 58,
            xpPerAction = 130.0,
            actionTimeMs = 7000L,
            resourceId = "cosmic_alignment",
            inputItems = mapOf("lunar_insight" to 1, "solar_insight" to 1),
        ),
        SkillAction(
            id = "compose_celestial_forecast",
            skillId = SkillId.ASTROLOGY,
            name = "Compose Celestial Forecast",
            levelRequired = 72,
            xpPerAction = 186.0,
            actionTimeMs = 8400L,
            resourceId = "celestial_forecast",
            inputItems = mapOf("cosmic_alignment" to 1, "star_chart" to 1),
        ),
        SkillAction(
            id = "forge_orbital_matrix",
            skillId = SkillId.ASTROLOGY,
            name = "Forge Orbital Matrix",
            levelRequired = 86,
            xpPerAction = 270.0,
            actionTimeMs = 10000L,
            resourceId = "orbital_matrix",
            inputItems = mapOf("celestial_forecast" to 1, "aether_pulse" to 1),
        ),
        SkillAction(
            id = "issue_stellar_edict",
            skillId = SkillId.ASTROLOGY,
            name = "Issue Stellar Edict",
            levelRequired = 96,
            xpPerAction = 395.0,
            actionTimeMs = 11800L,
            resourceId = "stellar_edict",
            inputItems = mapOf("orbital_matrix" to 1, "world_atlas_core" to 1, "void_rune" to 3),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
