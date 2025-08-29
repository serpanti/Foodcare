package ru.foodcare.foodcare.presentation.composable

import androidx.compose.runtime.Composable
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel

@Composable
fun SearchProductField(productViewModel: ProductViewModel,
                       dropDownList: @Composable () -> Unit = {}) {
    val startValue = productViewModel.key.value ?: ""
    val endSearch = null

    val search: () -> Unit = {
        productViewModel.key.value?.let {
            productViewModel.getProductByNameOrProduction(it)
        }
    }
    val onStartSearching: () -> Unit = {
        productViewModel.key.value = startValue
        search()
    }
    val onStopSearching: () -> Unit = {
        productViewModel.key.value = endSearch
        productViewModel.productsStatic.value = endSearch
    }
    val onValueChange: (String) -> Unit = {
        productViewModel.key.value = it
        search()
    }

    SearchField(onStartSearching, onStopSearching, startValue, onValueChange
    ) { productViewModel.key.value != null }
    dropDownList()
}
