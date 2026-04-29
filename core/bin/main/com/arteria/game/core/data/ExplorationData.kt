package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Exploration maps regions and uncovers relic-grade findings.
object ExplorationData {
    val SCOUT_NOTES = ItemDef("scout_notes", "Scout Notes", "Field notes from short-range scouting runs")
    val REGION_MAP = ItemDef("region_map", "Region Map", "Mapped local routes and resources")
    val ANCIENT_MARKER = ItemDef("ancient_marker", "Ancient Marker", "A marker stone from forgotten paths")
    val LOST_CACHE = ItemDef("lost_cache", "Lost Cache", "Recovered supplies from abandoned camps")
    val RUIN_FRAGMENT = ItemDef("ruin_fragment", "Ruin Fragment", "A fragment collected from old ruins")
    val FRONTIER_DOSSIER = ItemDef("frontier_dossier", "Frontier Dossier", "Detailed reports on distant zones")
    val STAR_COMPASS = ItemDef("star_compass", "Star Compass", "Compass calibrated to celestial navigation")
    val WORLD_ATLAS_CORE = ItemDef(
        "world_atlas_core",
        "World Atlas Core",
        "Master atlas core containing elite route intelligence",
    )

    val items: Map<String, ItemDef> = listOf(
        SCOUT_NOTES,
        REGION_MAP,
        ANCIENT_MARKER,
        LOST_CACHE,
        RUIN_FRAGMENT,
        FRONTIER_DOSSIER,
        STAR_COMPASS,
        WORLD_ATLAS_CORE,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "survey_nearby_trails",
            skillId = SkillId.EXPLORATION,
            name = "Survey Nearby Trails",
            levelRequired = 1,
            xpPerAction = 18.0,
            actionTimeMs = 3000L,
            resourceId = "scout_notes",
            inputItems = mapOf("cooked_sardine" to 1),
        ),
        SkillAction(
            id = "chart_local_routes",
            skillId = SkillId.EXPLORATION,
            name = "Chart Local Routes",
            levelRequired = 14,
            xpPerAction = 33.0,
            actionTimeMs = 3800L,
            resourceId = "region_map",
            inputItems = mapOf("scout_notes" to 1, "carrot" to 1),
        ),
        SkillAction(
            id = "trace_ancient_pathways",
            skillId = SkillId.EXPLORATION,
            name = "Trace Ancient Pathways",
            levelRequired = 28,
            xpPerAction = 54.0,
            actionTimeMs = 4700L,
            resourceId = "ancient_marker",
            inputItems = mapOf("region_map" to 1, "ancient_coin" to 1),
        ),
        SkillAction(
            id = "recover_lost_cache",
            skillId = SkillId.EXPLORATION,
            name = "Recover Lost Cache",
            levelRequired = 42,
            xpPerAction = 82.0,
            actionTimeMs = 5800L,
            resourceId = "lost_cache",
            inputItems = mapOf("ancient_marker" to 1, "cooked_trout" to 1),
        ),
        SkillAction(
            id = "search_ruined_outpost",
            skillId = SkillId.EXPLORATION,
            name = "Search Ruined Outpost",
            levelRequired = 58,
            xpPerAction = 122.0,
            actionTimeMs = 7000L,
            resourceId = "ruin_fragment",
            inputItems = mapOf("lost_cache" to 1, "wire_bundle" to 1),
        ),
        SkillAction(
            id = "compile_frontier_dossier",
            skillId = SkillId.EXPLORATION,
            name = "Compile Frontier Dossier",
            levelRequired = 72,
            xpPerAction = 176.0,
            actionTimeMs = 8400L,
            resourceId = "frontier_dossier",
            inputItems = mapOf("ruin_fragment" to 1, "battle_chant" to 1),
        ),
        SkillAction(
            id = "calibrate_star_compass",
            skillId = SkillId.EXPLORATION,
            name = "Calibrate Star Compass",
            levelRequired = 86,
            xpPerAction = 255.0,
            actionTimeMs = 10000L,
            resourceId = "star_compass",
            inputItems = mapOf("frontier_dossier" to 1, "star_chart" to 1),
        ),
        SkillAction(
            id = "assemble_world_atlas_core",
            skillId = SkillId.EXPLORATION,
            name = "Assemble World Atlas Core",
            levelRequired = 96,
            xpPerAction = 375.0,
            actionTimeMs = 11800L,
            resourceId = "world_atlas_core",
            inputItems = mapOf("star_compass" to 1, "chronicle_fateweave" to 1, "celestial_anthem" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
