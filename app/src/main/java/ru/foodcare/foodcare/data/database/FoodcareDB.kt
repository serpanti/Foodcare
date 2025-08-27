package ru.foodcare.foodcare.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.foodcare.foodcare.data.meal.Meal
import ru.foodcare.foodcare.data.meal.MealDAO
import ru.foodcare.foodcare.data.product.Product
import ru.foodcare.foodcare.data.product.ProductDAO

@Database(entities = [Product::class, Meal::class], version = 1)
abstract class FoodcareDB: RoomDatabase() {
    abstract fun productDAO(): ProductDAO
    abstract fun mealDAO(): MealDAO
}