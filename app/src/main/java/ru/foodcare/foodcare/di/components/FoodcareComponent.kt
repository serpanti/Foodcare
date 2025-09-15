package ru.foodcare.foodcare.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.foodcare.foodcare.di.modules.DateRepositoryModule
import ru.foodcare.foodcare.di.modules.FoodcareDBModule
import ru.foodcare.foodcare.di.modules.MealRepositoryModule
import ru.foodcare.foodcare.di.modules.ProductRepositoryModule
import ru.foodcare.foodcare.di.modules.WeightRepositoryModule
import ru.foodcare.foodcare.domain.date.DateRepository
import ru.foodcare.foodcare.domain.meal.MealRepository
import ru.foodcare.foodcare.domain.product.ProductRepository
import ru.foodcare.foodcare.domain.weight.WeightRepository
import javax.inject.Singleton

@Component(modules = [FoodcareDBModule::class,
    ProductRepositoryModule::class,
    MealRepositoryModule::class, DateRepositoryModule::class,
    WeightRepositoryModule::class])
@Singleton
interface FoodcareComponent {
    fun getWeightRepository() : WeightRepository
    fun getMealRepository() : MealRepository
    fun getDateRepository() : DateRepository
    fun getProductRepository() : ProductRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): FoodcareComponent
    }
}