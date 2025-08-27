package ru.foodcare.foodcare.presentation.mock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.meal.MealRepository
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel

class MockMealViewModel : MealViewModel(
    repository = object : MealRepository {
        override fun observeYears(): Flow<List<Int>> {
            TODO("Not yet implemented")
        }

        override fun observeMonths(year: Int): Flow<List<Int>> {
            TODO("Not yet implemented")
        }

        override fun observeDays(
            year: Int,
            month: Int
        ): Flow<List<Int>> {
            TODO("Not yet implemented")
        }

        override fun observeDay(
            year: Int,
            month: Int,
            day: Int
        ): Flow<List<Meal>> {
            TODO("Not yet implemented")
        }

        override suspend fun addMeal(meal: Meal) {
            TODO("Not yet implemented")
        }

        override suspend fun removeMeal(meal: Meal) {
            TODO("Not yet implemented")
        }

        override suspend fun updateMeal(meal: Meal) {
            TODO("Not yet implemented")
        }

    }, Dispatchers.IO)