package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel

@Composable
fun SearchProductField(productViewModel: ProductViewModel) {
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
}

@Composable
fun SearchProductFieldWithList(productViewModel: ProductViewModel,
                                   list: @Composable (visible: Boolean, close: () -> Unit,
                                                              products: List<Product>?) -> Unit) {
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

    val products = remember { productViewModel.productsStatic }
    val visible by remember { derivedStateOf { productViewModel.key.value != null } }
    list(visible, onStopSearching, products.value)
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
