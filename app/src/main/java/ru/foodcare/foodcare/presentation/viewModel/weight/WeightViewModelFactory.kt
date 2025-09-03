package ru.foodcare.foodcare.presentation.viewModel.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.foodcare.foodcare.domain.weight.WeightRepository
import kotlin.coroutines.CoroutineContext

class WeightViewModelFactory(private val weightRepository: WeightRepository,
                             private val context: CoroutineContext
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeightViewModel(weightRepository, context) as T
    }
}