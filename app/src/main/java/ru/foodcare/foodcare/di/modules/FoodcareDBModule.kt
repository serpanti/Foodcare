package ru.foodcare.foodcare.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.foodcare.foodcare.data.database.FoodcareDB
import javax.inject.Singleton

@Module
object FoodcareDBModule {
    @Provides
    @Singleton
    fun providesFoodcareDB(context: Context): FoodcareDB = Room.databaseBuilder(
        context,
        FoodcareDB::class.java,
        "foodcare.db"
    ).fallbackToDestructiveMigration(true)
        .build()

    @Provides
    fun providesProductDAO(db: FoodcareDB) = db.productDAO()

    @Provides
    fun providesMealDAO(db: FoodcareDB) = db.mealDAO()

    @Provides
    fun providesDateDAO(db: FoodcareDB) = db.dateDAO()

    @Provides
    fun providesWeightMeasurementDAO(db: FoodcareDB) = db.weightMeasurementDAO()
}