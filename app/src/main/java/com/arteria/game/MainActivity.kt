package com.arteria.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arteria.game.data.preferences.ThemePreference
import com.arteria.game.data.preferences.UserPreferences
import com.arteria.game.data.preferences.UserPreferencesRepository
import com.arteria.game.ui.ArteriaApp
import com.arteria.game.ui.theme.ArteriaTheme
import com.arteria.game.ui.theme.LocalArteriaDarkSpace
import com.arteria.game.ui.theme.LocalReduceMotion
import com.arteria.game.ui.theme.LocalUserPreferencesRepository
import com.arteria.game.ui.theme.rememberSystemReduceMotion

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArteriaRoot()
        }
    }
}

@Composable
private fun ArteriaRoot() {
    val context = LocalContext.current
    val prefsRepository = remember(context) {
        UserPreferencesRepository(context.applicationContext)
    }
    val prefs by prefsRepository.userPreferences.collectAsStateWithLifecycle(
        initialValue = UserPreferences.DEFAULT,
    )
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (prefs.themePreference) {
        ThemePreference.DARK -> true
        ThemePreference.FOLLOW_SYSTEM -> systemDark
    }
    val systemRm = rememberSystemReduceMotion()
    val reduceMotion = prefs.reduceMotion || systemRm

    CompositionLocalProvider(
        LocalUserPreferencesRepository provides prefsRepository,
        LocalArteriaDarkSpace provides darkTheme,
        LocalReduceMotion provides reduceMotion,
    ) {
        ArteriaTheme(darkTheme = darkTheme) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent,
            ) {
                ArteriaApp(Modifier.fillMaxSize())
            }
        }
    }
}
