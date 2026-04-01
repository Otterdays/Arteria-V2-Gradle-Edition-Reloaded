package com.arteria.game.data.preferences

import kotlinx.coroutines.flow.first

// [TRACE: master_settings_suggestions_doc.md — DataStore app prefs]

enum class ThemePreference {
    DARK,
    FOLLOW_SYSTEM,
}

data class UserPreferences(
    val themePreference: ThemePreference,
    val reduceMotion: Boolean,
    val hapticsEnabled: Boolean,
    val soundEnabled: Boolean,
    val showOfflineReport: Boolean,
    val idleSoundscapesEnabled: Boolean,
    /** DEBUG only: when true, offline catch-up ignores max duration cap. */
    val debugRemoveOfflineCap: Boolean,
) {
    companion object {
        val DEFAULT = UserPreferences(
            themePreference = ThemePreference.DARK,
            reduceMotion = false,
            hapticsEnabled = true,
            soundEnabled = true,
            showOfflineReport = true,
            idleSoundscapesEnabled = false,
            debugRemoveOfflineCap = false,
        )
    }
}

fun interface UserPreferencesProvider {
    suspend fun current(): UserPreferences
}

fun UserPreferencesRepository.asProvider(): UserPreferencesProvider {
    val repo = this
    return UserPreferencesProvider { repo.userPreferences.first() }
}
