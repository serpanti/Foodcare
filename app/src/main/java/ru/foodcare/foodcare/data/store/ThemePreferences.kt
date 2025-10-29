package ru.foodcare.foodcare.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "settings")

private val THEME_KEY = stringPreferencesKey("theme")

data class ThemePreferences @Inject constructor(private val context: Context) {
    val theme: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[THEME_KEY] ?: Themes.SYSTEM.toString() }

    suspend fun setDarkTheme() {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = Themes.DARK.toString()
        }
    }

    suspend fun setLightTheme() {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = Themes.LIGHT.toString()
        }
    }

    suspend fun setSystemTheme() {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = Themes.SYSTEM.toString()
        }
    }

    companion object {
        enum class Themes {
            LIGHT, DARK, SYSTEM
        }
    }
}
