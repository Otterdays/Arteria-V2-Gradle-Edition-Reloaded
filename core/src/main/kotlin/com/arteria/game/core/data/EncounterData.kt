package com.arteria.game.core.data

import com.arteria.game.core.model.EncounterLocation
import com.arteria.game.core.model.EnemyDef
import com.arteria.game.core.model.EnemyDrop

object EncounterData {

    const val LOCATION_SUNNY_MEADOW_BARN: String = "sunny_meadow_barn"
    const val ENEMY_BARN_RAT: String = "barn_rat"

    val locations: Map<String, EncounterLocation> = mapOf(
        LOCATION_SUNNY_MEADOW_BARN to EncounterLocation(
            id = LOCATION_SUNNY_MEADOW_BARN,
            name = "Sunny Meadow Barn",
            description = "Hay-scented boards, warm dust, and something scurrying in the straw.",
            enemyIds = listOf(ENEMY_BARN_RAT),
        ),
    )

    val enemies: Map<String, EnemyDef> = mapOf(
        ENEMY_BARN_RAT to EnemyDef(
            id = ENEMY_BARN_RAT,
            name = "Barn Rat",
            maxHp = 12,
            attack = 2,
            defence = 2,
            accuracy = 12,
            attackIntervalMs = 2_400L,
            xpPerKill = 18.0,
            drops = listOf(
                EnemyDrop(itemId = "rat_tail", chance = 0.35, minQty = 1, maxQty = 1),
                EnemyDrop(itemId = "bronze_sword", chance = 0.03, minQty = 1, maxQty = 1),
                EnemyDrop(itemId = "gold_charm", chance = 0.18, minQty = 1, maxQty = 1),
                EnemyDrop(itemId = "spirit_shard", chance = 0.45, minQty = 2, maxQty = 5),
            ),
        ),
    )

    fun location(id: String): EncounterLocation? = locations[id]

    fun enemy(id: String): EnemyDef? = enemies[id]
}
