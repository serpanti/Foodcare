package ru.foodcare.foodcare

import android.app.Application
import android.content.Context
import ru.foodcare.foodcare.di.components.DaggerFoodcareComponent
import ru.foodcare.foodcare.di.components.FoodcareComponent

class Foodcare: Application() {
    lateinit var foodcareComponent: FoodcareComponent
        private set

    override fun onCreate() {
        super.onCreate()
        foodcareComponent = DaggerFoodcareComponent.factory().create(this)
    }
}

val Context.foodcare
    get() = applicationContext as Foodcare
