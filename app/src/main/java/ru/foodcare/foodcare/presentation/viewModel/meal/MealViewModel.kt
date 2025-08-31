package ru.foodcare.foodcare.presentation.viewModel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.meal.MealRepository
import java.time.LocalDate
import kotlin.coroutines.CoroutineContext

open class MealViewModel(private val repository: MealRepository,
    private val context: CoroutineContext): ViewModel() {

    val observedYears = repository.observeYears()
        .flowOn(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _mealObserved: MutableStateFlow<Meal?> = MutableStateFlow(null)
    val mealObserved: StateFlow<Meal?> = _mealObserved.asStateFlow()

    fun onUpdateMeal(meal: Meal) {
        _mealObserved.value = meal
    }

    fun onAddMeal() {
        _mealObserved.value = null
    }

    private val _observedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val observedDate = _observedDate.asStateFlow()

    fun onObservedDateChanged(date: LocalDate) {
        _observedDate.value = date
    }

    fun observeMonths(year: Int): StateFlow<List<Int>> {
        return repository.observeMonths(year).flowOn(context)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun observeDays(year: Int, month: Int): StateFlow<List<Int>> {
        return repository.observeDays(year, month).flowOn(context)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun observeDay(date: LocalDate): StateFlow<List<Meal>> {
        return repository.observeDay(date.year, date.monthValue, date.dayOfMonth).flowOn(context)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )
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