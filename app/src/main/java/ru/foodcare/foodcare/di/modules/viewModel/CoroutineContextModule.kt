package ru.foodcare.foodcare.di.modules.viewModel

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Module
object CoroutineContextModule {
    @Provides
    @IODispatcher
    fun providesCoroutineContext(): CoroutineContext = Dispatchers.IO
}
