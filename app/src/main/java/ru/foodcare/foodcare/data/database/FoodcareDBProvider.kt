package ru.foodcare.foodcare.data.database

import android.content.Context
import androidx.room.Room

object FoodcareDBProvider {
    @Volatile
    private var instance: FoodcareDB? = null

    fun getInstance(context: Context): FoodcareDB {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                        context.applicationContext,
                        FoodcareDB::class.java,
                        "foodcare.db"
                    ).fallbackToDestructiveMigration(true)
                .build()
                .also {
                    instance = it
                }
        }
    }
}