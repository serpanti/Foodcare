package ru.foodcare.foodcare.presentation.viewModel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.meal.MealRepository
import kotlin.coroutines.CoroutineContext

open class MealViewModel(private val repository: MealRepository,
    private val context: CoroutineContext): ViewModel() {

    fun observeYears(): Flow<List<Int>> {
        return repository.observeYears()
    }

    fun observeMonths(year: Int): Flow<List<Int>> {
        return repository.observeMonths(year)
    }

    fun observeDays(year: Int, month: Int): Flow<List<Int>> {
        return repository.observeDays(year, month)
    }

    fun observeDay(year: Int, month: Int, day: Int): Flow<List<Meal>> {
        return repository.observeDay(year, month, day)
    }

    fun addMeal(product: Meal) {
        viewModelScope.launch(context) {
            repository.addMeal(product)
        }
    }

    fun removeMeal(product: Meal) {
        viewModelScope.launch(context) {
            repository.removeMeal(product)
        }
    }

    fun updateMeal(meal: Meal) {
        viewModelScope.launch(context) {
            repository.updateMeal(meal)
        }
    }
}