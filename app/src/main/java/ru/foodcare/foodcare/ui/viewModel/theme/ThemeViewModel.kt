package ru.foodcare.foodcare.ui.viewModel.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.data.store.ThemePreferences
import ru.foodcare.foodcare.di.modules.viewModel.IODispatcher
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ThemeViewModel @Inject constructor(
    private val prefs: ThemePreferences,
    @param:IODispatcher private val context: CoroutineContext) : ViewModel() {

    val theme: StateFlow<String> = prefs.theme
        .flowOn(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = ThemePreferences.Companion.Themes.SYSTEM.toString()
        )

    fun setDarkTheme() {
        viewModelScope.launch(context) {
            prefs.setDarkTheme()
        }
    }

    fun setLightTheme() {
        viewModelScope.launch(context) {
            prefs.setLightTheme()
        }
    }

    fun setSystemTheme() {
        viewModelScope.launch(context) {
            prefs.setSystemTheme()
        }
    }
}
