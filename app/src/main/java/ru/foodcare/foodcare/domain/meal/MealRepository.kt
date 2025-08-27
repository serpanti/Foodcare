package ru.foodcare.foodcare.domain.meal

import kotlinx.coroutines.flow.Flow

interface MealRepository {
    fun observeYears(): Flow<List<Int>>
    fun observeMonths(year: Int): Flow<List<Int>>
    fun observeDays(year: Int, month: Int): Flow<List<Int>>
    fun observeDay(year: Int, month: Int, day: Int): Flow<List<Meal>>
    suspend fun addMeal(meal: Meal)
    suspend fun removeMeal(meal: Meal)
    suspend fun updateMeal(meal: Meal)
}