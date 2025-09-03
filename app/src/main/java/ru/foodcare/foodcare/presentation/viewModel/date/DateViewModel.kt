package ru.foodcare.foodcare.presentation.viewModel.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import ru.foodcare.foodcare.domain.date.DateRepository
import java.time.LocalDate
import kotlin.coroutines.CoroutineContext

class DateViewModel(private val repository: DateRepository,
                    private val context: CoroutineContext): ViewModel() {
    val observedYears = repository.observeYears()
        .flowOn(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
}