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

@Composable
fun DropdownProducts(visible: Boolean, close: () -> Unit, products: List<Product>?,
                     modifier: Modifier = Modifier, offset: DpOffset,
                     onSelected: (Product) -> Unit) {
    DropdownMenu(visible, close, modifier = modifier,
        offset = offset) {
        if (products == null) {
            InfiniteLoading()
        } else {
            LazyColumn {
                items(products.size) { idx ->
                    TextButton({
                        onSelected(products[idx])
                        close()
                    }) {
                        val product = products[idx]
                        Text("%s %s".format(product.name, product.production))
                    }
                }
            }
        }
    }
}
