package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arteria.game.core.skill.SkillId
import com.arteria.game.ui.theme.ArteriaPalette

@Composable
fun SkillComingSoonDialog(
    skillId: SkillId,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ArteriaPalette.BgCard,
            modifier = modifier.border(
                1.dp,
                ArteriaPalette.AccentPrimary.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp),
            ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Coming Soon!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = ArteriaPalette.Gold,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = skillId.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.TextPrimary,
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = skillId.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextSecondary,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArteriaPalette.AccentPrimary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Got it")
                }
            }
        }
    }
}
