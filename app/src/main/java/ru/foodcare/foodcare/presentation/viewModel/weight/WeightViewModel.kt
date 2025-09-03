package ru.foodcare.foodcare.presentation.viewModel.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.weight.Weight
import ru.foodcare.foodcare.domain.weight.WeightRepository
import kotlin.coroutines.CoroutineContext

class WeightViewModel(private val weightRepository: WeightRepository,
                      private val context: CoroutineContext) : ViewModel() {
    fun observeDay(year: Int, month: Int, day: Int): Flow<List<Weight>> {
        return weightRepository.observeDay(year, month, day)
    }

    fun add(weight: Weight) {
        viewModelScope.launch(context) {
            weightRepository.add(weight)
        }
    }

    fun remove(weight: Weight) {
        viewModelScope.launch(context) {
            weightRepository.remove(weight)
        }
    }

    fun update(weight: Weight) {
        viewModelScope.launch(context) {
            weightRepository.update(weight)
        }
    }
}