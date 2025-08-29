package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel

@Composable
fun SearchProductField(productViewModel: ProductViewModel, modifier: Modifier = Modifier) {
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

    SearchField(onStartSearching, onStopSearching, startValue, onValueChange, modifier
    ) { productViewModel.key.value != null }
}

@Composable
fun SearchProductFieldWithList(productViewModel: ProductViewModel, modifier: Modifier = Modifier,
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

    SearchField(onStartSearching, onStopSearching, startValue, onValueChange, modifier
    ) { productViewModel.key.value != null }

    val products = remember { productViewModel.productsStatic }
    val visible by remember { derivedStateOf { productViewModel.key.value != null } }
    list(visible, onStopSearching, products.value)
}

@Composable
fun DropdownProducts(visible: Boolean, close: () -> Unit, products: List<Product>?,
                     modifier: Modifier = Modifier, offset: IntOffset,
                     onSelected: (Product) -> Unit) {
    if (visible) {
        Popup(onDismissRequest = {},
            offset = offset,
            alignment = Alignment.TopStart) {
            Box(modifier
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)) {
                if (products == null) {
                    InfiniteLoading()
                } else {
                    if (products.isEmpty()) {
                        Text("Нет совпадений", Modifier.align(Alignment.Center))
                    } else {
                        ProductButtonsLazyColumn(products) { product ->
                            onSelected(product)
                            close()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductButtonsLazyColumn(products: List<Product>,
                      onSelected: (Product) -> Unit) {
    LazyColumn {
        items(products.size) { idx ->
            TextButton({
                onSelected(products[idx])
            }) {
                val product = products[idx]
                Text("%s %s".format(product.name, product.production))
            }
            if (idx < products.size - 1) HorizontalDivider(1.dp, Color.Gray)
        }
    }
}
