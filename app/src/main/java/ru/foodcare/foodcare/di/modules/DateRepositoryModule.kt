package ru.foodcare.foodcare.di.modules

import dagger.Binds
import dagger.Module
import ru.foodcare.foodcare.data.date.DateRepositoryImpl
import ru.foodcare.foodcare.domain.date.DateRepository
import javax.inject.Singleton

@Module
interface DateRepositoryModule {
    @Binds
    @Singleton
    fun bindsDateRepository(dateRepositoryImpl: DateRepositoryImpl) : DateRepository
}