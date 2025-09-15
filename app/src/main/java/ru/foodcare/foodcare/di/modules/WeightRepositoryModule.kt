package ru.foodcare.foodcare.di.modules

import dagger.Binds
import dagger.Module
import ru.foodcare.foodcare.data.weightMeasurement.WeightRepositoryImpl
import ru.foodcare.foodcare.domain.weight.WeightRepository
import javax.inject.Singleton

@Module
interface WeightRepositoryModule {
    @Binds
    @Singleton
    fun bindsWeightRepository(
        weightRepositoryImpl: WeightRepositoryImpl
    ): WeightRepository
}