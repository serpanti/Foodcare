package ru.foodcare.foodcare.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import ru.foodcare.foodcare.domain.ProductRepository

open class ProductViewModel(private val repository: ProductRepository): ViewModel() {
    val products = repository.observeProducts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
