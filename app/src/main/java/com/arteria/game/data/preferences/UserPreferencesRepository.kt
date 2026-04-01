package com.arteria.game.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences",
)

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {

    constructor(context: Context) : this(context.applicationContext.userPreferencesDataStore)

    val userPreferences: Flow<UserPreferences> = dataStore.data.map { prefs ->
        UserPreferences(
            themePreference = when (prefs[Keys.THEME]) {
                KEY_THEME_FOLLOW -> ThemePreference.FOLLOW_SYSTEM
                else -> ThemePreference.DARK
            },
            reduceMotion = prefs[Keys.REDUCE_MOTION] ?: false,
            hapticsEnabled = prefs[Keys.HAPTICS] ?: true,
            soundEnabled = prefs[Keys.SOUND] ?: true,
            showOfflineReport = prefs[Keys.OFFLINE_REPORT] ?: true,
            idleSoundscapesEnabled = prefs[Keys.SOUNDSCAPES] ?: false,
            debugRemoveOfflineCap = prefs[Keys.DEBUG_OFFLINE_CAP] ?: false,
        )
    }

    suspend fun update(transform: (UserPreferences) -> UserPreferences) {
        dataStore.edit { prefs ->
            val current = UserPreferences(
                themePreference = when (prefs[Keys.THEME]) {
                    KEY_THEME_FOLLOW -> ThemePreference.FOLLOW_SYSTEM
                    else -> ThemePreference.DARK
                },
                reduceMotion = prefs[Keys.REDUCE_MOTION] ?: false,
                hapticsEnabled = prefs[Keys.HAPTICS] ?: true,
                soundEnabled = prefs[Keys.SOUND] ?: true,
                showOfflineReport = prefs[Keys.OFFLINE_REPORT] ?: true,
                idleSoundscapesEnabled = prefs[Keys.SOUNDSCAPES] ?: false,
                debugRemoveOfflineCap = prefs[Keys.DEBUG_OFFLINE_CAP] ?: false,
            )
            val next = transform(current)
            prefs[Keys.THEME] = when (next.themePreference) {
                ThemePreference.FOLLOW_SYSTEM -> KEY_THEME_FOLLOW
                ThemePreference.DARK -> KEY_THEME_DARK
            }
            prefs[Keys.REDUCE_MOTION] = next.reduceMotion
            prefs[Keys.HAPTICS] = next.hapticsEnabled
            prefs[Keys.SOUND] = next.soundEnabled
            prefs[Keys.OFFLINE_REPORT] = next.showOfflineReport
            prefs[Keys.SOUNDSCAPES] = next.idleSoundscapesEnabled
            prefs[Keys.DEBUG_OFFLINE_CAP] = next.debugRemoveOfflineCap
        }
    }

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        val HAPTICS = booleanPreferencesKey("haptics")
        val SOUND = booleanPreferencesKey("sound")
        val OFFLINE_REPORT = booleanPreferencesKey("offline_report")
        val SOUNDSCAPES = booleanPreferencesKey("soundscapes")
        val DEBUG_OFFLINE_CAP = booleanPreferencesKey("debug_offline_cap")
    }

    private companion object {
        const val KEY_THEME_DARK = "dark"
        const val KEY_THEME_FOLLOW = "follow_system"
    }
}
