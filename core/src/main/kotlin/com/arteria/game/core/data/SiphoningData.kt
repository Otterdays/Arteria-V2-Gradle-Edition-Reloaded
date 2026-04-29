package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Siphoning extracts ambient magic into progression essences.
object SiphoningData {
    val LEY_DROPLET = ItemDef("ley_droplet", "Ley Droplet", "A small drop of raw leyline energy")
    val ARCANE_RESIDUE = ItemDef("arcane_residue", "Arcane Residue", "Stable residue left after channeling")
    val RESONANCE_CORE = ItemDef("resonance_core", "Resonance Core", "A compacted pulse core")
    val PLANAR_FILAMENT = ItemDef("planar_filament", "Planar Filament", "Thread-like energy from thin rifts")
    val VOID_ESSENCE = ItemDef("void_essence", "Void Essence", "Dense dark essence from collapsed anomalies")
    val CELESTIAL_CHARGE = ItemDef("celestial_charge", "Celestial Charge", "Charged starlight crystal")
    val AETHER_PULSE = ItemDef("aether_pulse", "Aether Pulse", "High-frequency pulse from pure aether")
    val SINGULARITY_SPOOL = ItemDef(
        "singularity_spool",
        "Singularity Spool",
        "A tightly wound mass of impossible energy",
    )

    val items: Map<String, ItemDef> = listOf(
        LEY_DROPLET,
        ARCANE_RESIDUE,
        RESONANCE_CORE,
        PLANAR_FILAMENT,
        VOID_ESSENCE,
        CELESTIAL_CHARGE,
        AETHER_PULSE,
        SINGULARITY_SPOOL,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "siphon_ley_droplet",
            skillId = SkillId.SIPHONING,
            name = "Leyline Tap",
            levelRequired = 1,
            xpPerAction = 20.0,
            actionTimeMs = 2800L,
            resourceId = "ley_droplet",
            inputItems = mapOf("air_rune" to 5),
        ),
        SkillAction(
            id = "refine_arcane_residue",
            skillId = SkillId.SIPHONING,
            name = "Residue Refinement",
            levelRequired = 14,
            xpPerAction = 35.0,
            actionTimeMs = 3600L,
            resourceId = "arcane_residue",
            inputItems = mapOf("ley_droplet" to 1, "water_rune" to 5),
        ),
        SkillAction(
            id = "compress_resonance_core",
            skillId = SkillId.SIPHONING,
            name = "Resonance Compression",
            levelRequired = 28,
            xpPerAction = 56.0,
            actionTimeMs = 4600L,
            resourceId = "resonance_core",
            inputItems = mapOf("arcane_residue" to 1, "earth_rune" to 6),
        ),
        SkillAction(
            id = "weave_planar_filament",
            skillId = SkillId.SIPHONING,
            name = "Planar Weave",
            levelRequired = 42,
            xpPerAction = 86.0,
            actionTimeMs = 5600L,
            resourceId = "planar_filament",
            inputItems = mapOf("resonance_core" to 1, "cosmic_rune" to 3),
        ),
        SkillAction(
            id = "distill_void_essence",
            skillId = SkillId.SIPHONING,
            name = "Void Distillation",
            levelRequired = 58,
            xpPerAction = 128.0,
            actionTimeMs = 6800L,
            resourceId = "void_essence",
            inputItems = mapOf("planar_filament" to 1, "void_shard" to 1),
        ),
        SkillAction(
            id = "charge_celestial_flux",
            skillId = SkillId.SIPHONING,
            name = "Celestial Flux Charge",
            levelRequired = 72,
            xpPerAction = 185.0,
            actionTimeMs = 8200L,
            resourceId = "celestial_charge",
            inputItems = mapOf("void_essence" to 1, "star_essence" to 1),
        ),
        SkillAction(
            id = "shape_aether_pulse",
            skillId = SkillId.SIPHONING,
            name = "Aether Pulse Shaping",
            levelRequired = 86,
            xpPerAction = 270.0,
            actionTimeMs = 9800L,
            resourceId = "aether_pulse",
            inputItems = mapOf("celestial_charge" to 1, "aether_rune" to 2),
        ),
        SkillAction(
            id = "spin_singularity_spool",
            skillId = SkillId.SIPHONING,
            name = "Singularity Spin",
            levelRequired = 96,
            xpPerAction = 395.0,
            actionTimeMs = 11600L,
            resourceId = "singularity_spool",
            inputItems = mapOf("aether_pulse" to 1, "chronicle_fateweave" to 1, "void_rune" to 3),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
