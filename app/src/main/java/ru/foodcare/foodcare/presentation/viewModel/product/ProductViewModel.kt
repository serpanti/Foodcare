package ru.foodcare.foodcare.presentation.viewModel.product

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.product.ProductRepository
import kotlin.coroutines.CoroutineContext

open class ProductViewModel(private val repository: ProductRepository, private val context: CoroutineContext): ViewModel() {
    val products = repository.observeProducts().flowOn(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
    )

    var productObserved: Product? = null
        get() {
            val retField = field
            field = null
            return retField
        }

    var key: MutableState<String?> = mutableStateOf(null)

    var productsStatic: MutableState<List<Product>?> = mutableStateOf(null)

    fun getProductByNameOrProduction(nameOrProduction: String) {
        viewModelScope.launch(context) {
            productsStatic.value =
                (repository.getProductByName(nameOrProduction) +
                repository.getProductByProduction(nameOrProduction)).distinct()
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