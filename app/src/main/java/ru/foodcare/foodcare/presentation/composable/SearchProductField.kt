package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
    val key = productViewModel.key.collectAsState()
    val isOpened = remember { derivedStateOf { key.value.isNotEmpty() } }
    val defaultKey = ""

    val onStartSearching: () -> Unit = {}
    val onStopSearching: () -> Unit = {
        productViewModel.onKeyChanged(defaultKey)
    }
    val onValueChange: (String) -> Unit = { newKey ->
        productViewModel.onKeyChanged(newKey)
    }

    SearchField(onStartSearching = onStartSearching,
        onStopSearching = onStopSearching,
        startValue = key.value,
        onValueChange = onValueChange,
        modifier = modifier
    ) { isOpened.value }
}

@Composable
fun SearchProductFieldWithList(productViewModel: ProductViewModel, modifier: Modifier = Modifier,
                                   list: @Composable (visible: Boolean, close: () -> Unit,
                                                              products: List<Product>?) -> Unit) {
    val key = productViewModel.key.collectAsState()
    val productsByQuery = productViewModel.productsByQuery.collectAsState()
    val isOpened = remember { derivedStateOf { key.value.isNotEmpty() } }
    val defaultKey = ""

    val onStartSearching: () -> Unit = {}
    val onStopSearching: () -> Unit = {
        productViewModel.onKeyChanged(defaultKey)
    }
    val onValueChange: (String) -> Unit = { newKey ->
        productViewModel.onKeyChanged(newKey)
    }

    SearchField(onStartSearching = onStartSearching,
        onStopSearching = onStopSearching,
        startValue = key.value,
        onValueChange = onValueChange,
        modifier = modifier
    ) { isOpened.value }

    list(isOpened.value, onStopSearching, productsByQuery.value)
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
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable {
                onSelected(products[idx])
            }
                .padding(10.dp)) {
                val product = products[idx]
                Row(modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start) {
                    Text("%s %s".format(product.name, product.production))
                }
            }
            if (idx < products.size - 1) HorizontalDivider(1.dp, Color.Gray)
        }
    }
}
