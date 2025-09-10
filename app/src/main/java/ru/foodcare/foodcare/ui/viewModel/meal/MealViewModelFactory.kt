package ru.foodcare.foodcare.ui.viewModel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.foodcare.foodcare.domain.meal.MealRepository
import kotlin.coroutines.CoroutineContext

class MealViewModelFactory(private val repository: MealRepository,
                           private val context: CoroutineContext): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(repository, context) as T
    }
}