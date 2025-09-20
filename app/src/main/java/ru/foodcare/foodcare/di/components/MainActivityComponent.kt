package ru.foodcare.foodcare.di.components

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import ru.foodcare.foodcare.di.modules.themes.ThemesModule
import ru.foodcare.foodcare.di.modules.viewModel.CoroutineContextModule
import ru.foodcare.foodcare.di.modules.viewModel.ViewModelFactoryModule
import ru.foodcare.foodcare.di.modules.viewModel.ViewModelModule

@MainActivityScope
@Subcomponent(modules = [ViewModelModule::class,
    CoroutineContextModule::class,
    ViewModelFactoryModule::class,
    ThemesModule::class])
interface MainActivityComponent {
    fun getViewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityComponent
    }
}