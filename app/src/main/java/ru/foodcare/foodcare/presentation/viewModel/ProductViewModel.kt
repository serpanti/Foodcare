package ru.foodcare.foodcare.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.Product
import ru.foodcare.foodcare.domain.ProductRepository
import java.lang.Exception

class ProductViewModel(private val repository: ProductRepository): ViewModel() {
    private val _productsUIState = MutableStateFlow<ProductsUIState>(ProductsUIState())
    val productsUIState = _productsUIState.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _productsUIState.value = try {
                ProductsUIState(ProductsStatus.Loaded, repository.getProducts())
            } catch (_: Exception) {
                ProductsUIState(ProductsStatus.Error)
            }
        }
    }
}

sealed class ProductsStatus {
    object Loading : ProductsStatus()
    object Loaded : ProductsStatus()
    object Error : ProductsStatus()
}

data class ProductsUIState(
    val status: ProductsStatus = ProductsStatus.Loading,
    val products: List<Product> = emptyList()
)
