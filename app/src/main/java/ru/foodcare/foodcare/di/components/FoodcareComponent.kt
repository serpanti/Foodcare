package ru.foodcare.foodcare.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.foodcare.foodcare.di.modules.repositories.DateRepositoryModule
import ru.foodcare.foodcare.di.modules.db.FoodcareDBModule
import ru.foodcare.foodcare.di.modules.repositories.MealRepositoryModule
import ru.foodcare.foodcare.di.modules.repositories.ProductRepositoryModule
import ru.foodcare.foodcare.di.modules.repositories.WeightRepositoryModule
import ru.foodcare.foodcare.domain.date.DateRepository
import ru.foodcare.foodcare.domain.meal.MealRepository
import ru.foodcare.foodcare.domain.product.ProductRepository
import ru.foodcare.foodcare.domain.weight.WeightRepository
import javax.inject.Singleton

@Component(modules = [FoodcareDBModule::class,
    ProductRepositoryModule::class,
    MealRepositoryModule::class, DateRepositoryModule::class,
    WeightRepositoryModule::class,
    FoodcareSubcomponentsModule::class])
@Singleton
interface FoodcareComponent {
    fun getWeightRepository() : WeightRepository
    fun getMealRepository() : MealRepository
    fun getDateRepository() : DateRepository
    fun getProductRepository() : ProductRepository
    fun getMainActivityComponentFactory() : MainActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): FoodcareComponent
    }
}