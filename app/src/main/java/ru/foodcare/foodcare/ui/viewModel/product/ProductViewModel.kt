package ru.foodcare.foodcare.ui.viewModel.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.di.modules.viewModel.IODispatcher
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.product.ProductRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    @param:IODispatcher private val context: CoroutineContext): ViewModel() {
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

    private val _key: MutableStateFlow<String> = MutableStateFlow("")
    val key: StateFlow<String> = _key.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val productsByQuery: StateFlow<List<Product>> = key
        .debounce(300)
        .flatMapLatest { query ->
            repository.observeProductsByName(query)
                .combine(
                    repository.observeProductsByProduction(query)
                ) { listByName, listByProduction ->
                    (listByName + listByProduction).distinct()
                }
        }
        .flowOn(context)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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