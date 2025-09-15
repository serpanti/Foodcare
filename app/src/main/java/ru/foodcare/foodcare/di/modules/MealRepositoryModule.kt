package ru.foodcare.foodcare.di.modules

import dagger.Binds
import dagger.Module
import ru.foodcare.foodcare.data.meal.MealRepositoryImpl
import ru.foodcare.foodcare.domain.meal.MealRepository
import javax.inject.Singleton

@Module
interface MealRepositoryModule {
    @Binds
    @Singleton
    fun bindsMealRepository(mealRepositoryImpl: MealRepositoryImpl) : MealRepository
}