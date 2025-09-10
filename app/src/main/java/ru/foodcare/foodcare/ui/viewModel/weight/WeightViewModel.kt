package ru.foodcare.foodcare.ui.viewModel.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.weight.Weight
import ru.foodcare.foodcare.domain.weight.WeightRepository
import java.time.LocalDate
import kotlin.coroutines.CoroutineContext

class WeightViewModel(private val weightRepository: WeightRepository,
                      private val context: CoroutineContext) : ViewModel() {
    private val _weightObserved: MutableStateFlow<Weight?> = MutableStateFlow(null)
    val weightObserved: StateFlow<Weight?> = _weightObserved.asStateFlow()

    fun onUpdateWeight(weight: Weight) {
        _weightObserved.value = weight
    }

    fun onAddWeight() {
        _weightObserved.value = null
    }
                          
    fun observeDay(date: LocalDate): StateFlow<List<Weight>> {
        return weightRepository.observeDay(date.year, date.monthValue, date.dayOfMonth)
            .flowOn(context)
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
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