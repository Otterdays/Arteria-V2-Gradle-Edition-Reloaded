package com.arteria.game.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.arteria.game.data.preferences.UserPreferencesRepository

/** When true, use dark space gradients and Docking starfield palette. */
val LocalArteriaDarkSpace = compositionLocalOf { true }

/** Effective reduced-motion (user pref OR system animator scale). */
val LocalReduceMotion = compositionLocalOf { false }

val LocalUserPreferencesRepository = staticCompositionLocalOf<UserPreferencesRepository> {
    error("LocalUserPreferencesRepository not provided")
}
