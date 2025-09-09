package ru.foodcare.foodcare.presentation.viewModel.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.product.ProductRepository
import kotlin.coroutines.CoroutineContext

open class ProductViewModel(private val repository: ProductRepository,
                            private val context: CoroutineContext): ViewModel() {
    val products = repository.observeProducts().flowOn(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
    )

    private val _lastChosenProduct: MutableStateFlow<Product?> = MutableStateFlow(null)
    val lastChosenProduct: StateFlow<Product?> = _lastChosenProduct.asStateFlow()

    fun onUpdateProduct(product: Product) {
        _lastChosenProduct.value = product
    }

    fun onAddProduct() {
        _lastChosenProduct.value = null
    }

    private val _productsByQuery: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val productsByQuery: StateFlow<List<Product>> = _productsByQuery.asStateFlow()

    private val _key: MutableStateFlow<String> = MutableStateFlow("")
    val key: StateFlow<String> = _key.asStateFlow()

    init {
        viewModelScope.launch(context) {
            @OptIn(FlowPreview::class)
            key.debounce(300)
//                раскомментировать, если key перестанет быть stateFlow
//                .distinctUntilChanged()
                .collect { query ->
                    _productsByQuery.value =
                        (repository.getProductByName(query) +
                        repository.getProductByProduction(query))
                        .distinct()
                }
        }
    }

    fun onKeyChanged(newKey: String) {
        _key.value = newKey
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