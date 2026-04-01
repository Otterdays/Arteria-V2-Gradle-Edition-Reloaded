package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object ScavengingData {
    val RUST_SCRAP = ItemDef("rust_scrap", "Rust Scrap", "Corroded metal bits — something useful hides inside")
    val WIRE_BUNDLE = ItemDef("wire_bundle", "Wire Bundle", "Salvaged copper and iron wire")
    val COGWORK_PIECE = ItemDef("cogwork_piece", "Cogwork Piece", "Tiny gears from forgotten devices")
    val REINFORCED_PLATE = ItemDef(
        "reinforced_plate",
        "Reinforced Plate",
        "Armor fragment worth melting down",
    )
    val ANCIENT_COIN = ItemDef("ancient_coin", "Ancient Coin", "Face worn smooth by time")
    val VOID_SHARD = ItemDef("void_shard", "Void Shard", "Glassy fragment that drinks light")
    val MYTHIC_RELIC = ItemDef("mythic_relic", "Mythic Relic", "A scrap that hums with old power")
    val ECHO_ARTIFACT = ItemDef(
        "echo_artifact",
        "Echo Artifact",
        "Salvage from nowhere — or everywhere",
    )

    val items: Map<String, ItemDef> = listOf(
        RUST_SCRAP,
        WIRE_BUNDLE,
        COGWORK_PIECE,
        REINFORCED_PLATE,
        ANCIENT_COIN,
        VOID_SHARD,
        MYTHIC_RELIC,
        ECHO_ARTIFACT,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction("scavenge_rust", SkillId.SCAVENGING, "Junk Heap", 1, 25.0, 3000L, "rust_scrap"),
        SkillAction("scavenge_wire", SkillId.SCAVENGING, "Wire Nest", 15, 37.5, 4000L, "wire_bundle"),
        SkillAction("scavenge_cogs", SkillId.SCAVENGING, "Broken Workshop", 30, 67.5, 5400L, "cogwork_piece"),
        SkillAction(
            "scavenge_plate",
            SkillId.SCAVENGING,
            "Battlefield Scrap",
            45,
            100.0,
            6000L,
            "reinforced_plate",
        ),
        SkillAction("scavenge_coins", SkillId.SCAVENGING, "Old Road", 60, 175.0, 7200L, "ancient_coin"),
        SkillAction("scavenge_void", SkillId.SCAVENGING, "Rift Debris", 75, 250.0, 9000L, "void_shard"),
        SkillAction(
            "scavenge_mythic",
            SkillId.SCAVENGING,
            "Ruin Core",
            85,
            380.0,
            10800L,
            "mythic_relic",
        ),
        SkillAction(
            "scavenge_echo",
            SkillId.SCAVENGING,
            "Echo Dump",
            95,
            500.0,
            14400L,
            "echo_artifact",
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
