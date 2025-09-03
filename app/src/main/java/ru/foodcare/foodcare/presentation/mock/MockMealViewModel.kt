package ru.foodcare.foodcare.presentation.mock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.meal.MealRepository
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import java.time.LocalDate

class MockMealViewModel : MealViewModel(
    repository = object : MealRepository {
        override fun observeDay(
            year: Int,
            month: Int,
            day: Int
        ): Flow<List<Meal>> {
            return flowOf(listOf(Meal(1, Product("Honey", "Farm",
                100, Product.Companion.UnitType.Milliliter, 584.0, 14.0, 30.0, 78.0, 2.0), 2.6,
                LocalDate.of(2023, 4, 18), 22, 56, 34)))
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