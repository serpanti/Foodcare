package ru.foodcare.foodcare.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import ru.foodcare.foodcare.domain.ProductRepository
import kotlin.coroutines.CoroutineContext

open class ProductViewModel(private val repository: ProductRepository, private val context: CoroutineContext): ViewModel() {
    val products = repository.observeProducts().flowOn(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
    )
}
