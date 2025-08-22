package ru.foodcare.foodcare.data.product

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class FoodcareDB: RoomDatabase() {
    abstract fun productDAO(): ProductDAO
}