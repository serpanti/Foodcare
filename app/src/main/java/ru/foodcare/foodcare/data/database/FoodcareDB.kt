package ru.foodcare.foodcare.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.foodcare.foodcare.data.product.Product
import ru.foodcare.foodcare.data.product.ProductDAO

@Database(entities = [Product::class], version = 1)
abstract class FoodcareDB: RoomDatabase() {
    abstract fun productDAO(): ProductDAO
}