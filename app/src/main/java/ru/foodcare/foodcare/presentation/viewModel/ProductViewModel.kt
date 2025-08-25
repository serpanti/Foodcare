package ru.foodcare.foodcare.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.Product
import ru.foodcare.foodcare.domain.ProductRepository
import kotlin.coroutines.CoroutineContext

open class ProductViewModel(private val repository: ProductRepository, private val context: CoroutineContext): ViewModel() {
    val products = repository.observeProducts().flowOn(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
    )

    var productObserved: Product? = null
        get() {
            val retField = field
            field = null
            return retField
        }

    fun getProductByName(name: String) {
        viewModelScope.launch(context) {
            repository.getProductByName(name)
        }
    }

    fun getProductByProduction(production: String) {
        viewModelScope.launch(context) {
            repository.getProductByProduction(production)
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(context) {
            repository.addProduct(product)
        }
    }

    fun removeProduct(product: Product) {
        viewModelScope.launch(context) {
            repository.removeProduct(product)
        }
    }

    fun updateProduct(newProduct: Product, oldProduct: Product) {
        viewModelScope.launch(context) {
            repository.updateProduct(newProduct, oldProduct)
        }
    }
}
