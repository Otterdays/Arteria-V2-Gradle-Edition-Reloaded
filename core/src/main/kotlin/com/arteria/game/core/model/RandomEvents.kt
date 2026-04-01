package com.arteria.game.core.model

import com.arteria.game.core.skill.SkillId

data class RandomEvent(
    val id: String,
    val title: String,
    val message: String,
    val skillId: SkillId?,
    val effect: EventEffect,
    val weight: Int = 1,
)

sealed class EventEffect {
    data class XpBonus(val multiplier: Double) : EventEffect()
    data class ResourceBonus(val itemId: String, val amount: Int) : EventEffect()
    data class ResourceLoss(val itemId: String, val amount: Int) : EventEffect()
    data class TemporaryBuff(val durationMs: Long, val description: String) : EventEffect()
    object Nothing : EventEffect()
}

data class ActiveRandomEvent(
    val event: RandomEvent,
    val triggeredAt: Long = System.currentTimeMillis(),
)

object RandomEventRegistry {

    val all: List<RandomEvent> = listOf(
        RandomEvent(
            id = "mysterious_traveler",
            title = "Mysterious Traveler",
            message = "A hooded figure approaches and offers you a gift of knowledge.",
            skillId = null,
            effect = EventEffect.XpBonus(1.5),
            weight = 2,
        ),
        RandomEvent(
            id = "lucky_find",
            title = "Lucky Find!",
            message = "You stumble upon a rare deposit while working.",
            skillId = null,
            effect = EventEffect.ResourceBonus("rare_gem", 1),
            weight = 1,
        ),
        RandomEvent(
            id = "tool_break",
            title = "Tool Wear",
            message = "Your tool shows signs of wear, but you press on.",
            skillId = null,
            effect = EventEffect.ResourceLoss("copper_ore", 2),
            weight = 3,
        ),
        RandomEvent(
            id = "inspiration",
            title = "Sudden Inspiration",
            message = "A flash of insight boosts your efficiency!",
            skillId = null,
            effect = EventEffect.TemporaryBuff(60_000L, "+25% XP for 60s"),
            weight = 2,
        ),
        RandomEvent(
            id = "cosmic_alignment",
            title = "Cosmic Alignment",
            message = "The stars align in your favor. Everything feels easier.",
            skillId = null,
            effect = EventEffect.XpBonus(2.0),
            weight = 1,
        ),
        RandomEvent(
            id = "minor_setback",
            title = "Minor Setback",
            message = "A small distraction costs you some progress.",
            skillId = null,
            effect = EventEffect.Nothing,
            weight = 4,
        ),
    )

    fun getById(id: String): RandomEvent? = all.find { it.id == id }

    private val totalWeight = all.sumOf { it.weight }

    fun getRandom(): RandomEvent {
        var roll = (0 until totalWeight).random()
        for (event in all) {
            roll -= event.weight
            if (roll < 0) return event
        }
        return all.last()
    }
}
