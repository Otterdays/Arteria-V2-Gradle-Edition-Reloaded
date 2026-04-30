package com.arteria.game.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arteria.game.core.model.Achievement
import com.arteria.game.ui.theme.AchievementDecor
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.theme.LocalReduceMotion

// [TRACE: DOCS/SUMMARY.md — Chronicle / achievements player feedback]

@Composable
fun AchievementUnlockBanner(
    achievement: Achievement?,
    modifier: Modifier = Modifier,
    /** Tap opens Chronicle when non-null — banner shows “View” hint. */
    onOpenChronicle: (() -> Unit)? = null,
) {
    val reduceMotion = LocalReduceMotion.current
    val visible = achievement != null
    val enter = fadeIn(animationSpec = tween(if (reduceMotion) 80 else 220)) +
        if (reduceMotion) {
            expandVertically(animationSpec = tween(80))
        } else {
            expandVertically(animationSpec = tween(280))
        }
    val exit =
        fadeOut(animationSpec = tween(if (reduceMotion) 80 else 200)) + shrinkVertically(
            animationSpec = tween(if (reduceMotion) 80 else 220),
        )

    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit,
        modifier = modifier,
    ) {
        val meta = achievement ?: return@AnimatedVisibility
        val accent = AchievementDecor.rarityAccent(meta.rarity)
        val shape = RoundedCornerShape(14.dp)

        Surface(
            color = ArteriaPalette.BgCard,
            shadowElevation = 6.dp,
            tonalElevation = 2.dp,
            shape = shape,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(shape)
                .border(2.dp, accent.copy(alpha = 0.92f), shape)
                .then(
                    if (onOpenChronicle != null) {
                        Modifier.clickable(onClick = onOpenChronicle)
                    } else {
                        Modifier
                    },
                ),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = meta.icon, fontSize = 36.sp, modifier = Modifier.size(44.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "ACHIEVEMENT UNLOCKED",
                        style = MaterialTheme.typography.labelSmall,
                        color = ArteriaPalette.Gold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = meta.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = ArteriaPalette.TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = meta.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = meta.rarity.label.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = accent,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (onOpenChronicle != null) {
                            Text(
                                text = "Tap · Chronicle",
                                style = MaterialTheme.typography.labelSmall,
                                color = ArteriaPalette.TextSecondary,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
    }
}
