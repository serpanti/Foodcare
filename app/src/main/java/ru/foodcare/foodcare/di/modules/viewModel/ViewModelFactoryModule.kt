package ru.foodcare.foodcare.di.modules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.foodcare.foodcare.di.components.MainActivityScope
import javax.inject.Provider

@Module
object ViewModelFactoryModule {
    @MainActivityScope
    @Provides
    fun providesViewModelFactory(
        viewModels: Map<Class<out ViewModel>,
                @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val provider = viewModels[modelClass]
                    ?: viewModels.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
                    ?: throw IllegalArgumentException("Unknown ViewModel class: $modelClass")

                @Suppress("UNCHECKED_CAST")
                return provider.get() as T
            }
        }
    }
}