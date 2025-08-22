package ru.foodcare.foodcare.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.foodcare.foodcare.domain.ProductRepository
import kotlin.coroutines.CoroutineContext

class ProductViewModelFactory(private val repository: ProductRepository,
                              private val context: CoroutineContext): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(repository, context) as T
    }
}
