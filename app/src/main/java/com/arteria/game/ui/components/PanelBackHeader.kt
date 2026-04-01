package com.arteria.game.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.theme.ArteriaPalette

@Composable
fun PanelBackHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    overline: String? = null,
    subtitle: String? = null,
    backTint: Color = ArteriaPalette.TextSecondary,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = backTint,
            )
        }
        Column {
            if (!overline.isNullOrBlank()) {
                Text(
                    text = overline,
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.Gold,
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = ArteriaPalette.TextPrimary,
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
            }
        }
    }
}
