package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Tinkering turns salvage into utility gadgets.
object TinkeringData {
    val SPARK_COIL = ItemDef("spark_coil", "Spark Coil", "A wound conductive coil for simple devices")
    val GEAR_ASSEMBLY = ItemDef("gear_assembly", "Gear Assembly", "Linked gears tuned for reliable motion")
    val SENSOR_LENS = ItemDef("sensor_lens", "Sensor Lens", "A polished lens for precision mechanisms")
    val AUTO_HAMMER = ItemDef("auto_hammer", "Auto Hammer", "Clockwork hammer that swings on its own")
    val FIELD_DRONE = ItemDef("field_drone", "Field Drone", "A helper drone for repetitive labor")
    val ARC_BATTERY = ItemDef("arc_battery", "Arc Battery", "A compact battery charged with runes")
    val RIFT_ANCHOR = ItemDef("rift_anchor", "Rift Anchor", "Stabilizes local reality for machinery")
    val CHRONO_REGULATOR = ItemDef(
        "chrono_regulator",
        "Chrono Regulator",
        "Synchronizes mechanisms against temporal drift",
    )

    val items: Map<String, ItemDef> = listOf(
        SPARK_COIL,
        GEAR_ASSEMBLY,
        SENSOR_LENS,
        AUTO_HAMMER,
        FIELD_DRONE,
        ARC_BATTERY,
        RIFT_ANCHOR,
        CHRONO_REGULATOR,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "wind_spark_coil",
            skillId = SkillId.TINKERING,
            name = "Wind Spark Coil",
            levelRequired = 1,
            xpPerAction = 22.0,
            actionTimeMs = 2800L,
            resourceId = "spark_coil",
            inputItems = mapOf("wire_bundle" to 2, "bronze_bar" to 1),
        ),
        SkillAction(
            id = "assemble_gear_assembly",
            skillId = SkillId.TINKERING,
            name = "Assemble Gear Assembly",
            levelRequired = 14,
            xpPerAction = 38.0,
            actionTimeMs = 3600L,
            resourceId = "gear_assembly",
            inputItems = mapOf("cogwork_piece" to 2, "iron_bar" to 1),
        ),
        SkillAction(
            id = "polish_sensor_lens",
            skillId = SkillId.TINKERING,
            name = "Polish Sensor Lens",
            levelRequired = 28,
            xpPerAction = 60.0,
            actionTimeMs = 4500L,
            resourceId = "sensor_lens",
            inputItems = mapOf("cut_sapphire" to 1, "wire_bundle" to 1),
        ),
        SkillAction(
            id = "build_auto_hammer",
            skillId = SkillId.TINKERING,
            name = "Build Auto Hammer",
            levelRequired = 42,
            xpPerAction = 92.0,
            actionTimeMs = 5600L,
            resourceId = "auto_hammer",
            inputItems = mapOf("gear_assembly" to 1, "steel_bar" to 1, "spark_coil" to 1),
        ),
        SkillAction(
            id = "build_field_drone",
            skillId = SkillId.TINKERING,
            name = "Build Field Drone",
            levelRequired = 58,
            xpPerAction = 136.0,
            actionTimeMs = 6800L,
            resourceId = "field_drone",
            inputItems = mapOf("gear_assembly" to 1, "sensor_lens" to 1, "reinforced_plate" to 1),
        ),
        SkillAction(
            id = "charge_arc_battery",
            skillId = SkillId.TINKERING,
            name = "Charge Arc Battery",
            levelRequired = 72,
            xpPerAction = 195.0,
            actionTimeMs = 8200L,
            resourceId = "arc_battery",
            inputItems = mapOf("spark_coil" to 1, "fire_rune" to 6, "water_rune" to 6),
        ),
        SkillAction(
            id = "forge_rift_anchor",
            skillId = SkillId.TINKERING,
            name = "Forge Rift Anchor",
            levelRequired = 86,
            xpPerAction = 280.0,
            actionTimeMs = 9800L,
            resourceId = "rift_anchor",
            inputItems = mapOf("arc_battery" to 1, "void_shard" to 2, "aether_rune" to 2),
        ),
        SkillAction(
            id = "tune_chrono_regulator",
            skillId = SkillId.TINKERING,
            name = "Tune Chrono Regulator",
            levelRequired = 96,
            xpPerAction = 405.0,
            actionTimeMs = 11600L,
            resourceId = "chrono_regulator",
            inputItems = mapOf("rift_anchor" to 1, "cosmic_rune" to 4, "mythic_relic" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
