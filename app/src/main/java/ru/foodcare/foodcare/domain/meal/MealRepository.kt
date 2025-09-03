package ru.foodcare.foodcare.domain.meal

import kotlinx.coroutines.flow.Flow

interface MealRepository {
    fun observeDay(year: Int, month: Int, day: Int): Flow<List<Meal>>
    suspend fun addMeal(meal: Meal)
    suspend fun removeMeal(meal: Meal)
    suspend fun updateMeal(meal: Meal)
}