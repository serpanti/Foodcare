package ru.foodcare.foodcare.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.foodcare.foodcare.di.modules.repositories.DateRepositoryModule
import ru.foodcare.foodcare.di.modules.db.FoodcareDBModule
import ru.foodcare.foodcare.di.modules.repositories.BackupRepositoryModule
import ru.foodcare.foodcare.di.modules.repositories.MealRepositoryModule
import ru.foodcare.foodcare.di.modules.repositories.ProductRepositoryModule
import ru.foodcare.foodcare.di.modules.repositories.WeightRepositoryModule
import javax.inject.Singleton

@Component(modules = [FoodcareDBModule::class,
    ProductRepositoryModule::class,
    MealRepositoryModule::class, DateRepositoryModule::class,
    WeightRepositoryModule::class,
    BackupRepositoryModule::class,
    FoodcareSubcomponentsModule::class])
@Singleton
interface FoodcareComponent {
    fun getMainActivityComponentFactory() : MainActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): FoodcareComponent
    }
}