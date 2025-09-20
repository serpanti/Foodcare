package ru.foodcare.foodcare.di.modules.themes

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.foodcare.foodcare.data.store.ThemePreferences
import ru.foodcare.foodcare.di.components.MainActivityScope

@Module
class ThemesModule {
    @Provides
    @MainActivityScope
    fun providesThemePreferences(context: Context) : ThemePreferences = ThemePreferences(context)
}
