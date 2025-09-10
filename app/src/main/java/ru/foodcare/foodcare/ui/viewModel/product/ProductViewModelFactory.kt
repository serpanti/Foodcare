package ru.foodcare.foodcare.ui.viewModel.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.foodcare.foodcare.domain.product.ProductRepository
import kotlin.coroutines.CoroutineContext

class ProductViewModelFactory(private val repository: ProductRepository,
                              private val context: CoroutineContext
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(repository, context) as T
    }
}