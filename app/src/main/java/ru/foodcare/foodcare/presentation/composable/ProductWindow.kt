package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel

@Composable
fun ProductsWindow(
    viewModel: ProductViewModel,
    openProductEditor: () -> Unit
) {
    var products = if (viewModel.key.value == null) {
        viewModel.products.collectAsState()
    } else {
        remember { viewModel.productsStatic }
    }

    if (products.value == null) {
        InfiniteLoading()
    } else {
        products.value?.let {
            Products(viewModel, it, openProductEditor)
        }
    }
}

@Composable
fun Products(viewModel: ProductViewModel, products: List<Product>,
             openEditor: () -> Unit = {}, lazyListState: LazyListState = rememberLazyListState()) {

    Box(modifier = Modifier.fillMaxSize()) {
        ProductsList(viewModel, products, openEditor, lazyListState)

        SwipeToStartButton(modifier = Modifier.align(Alignment.BottomCenter), lazyListState)

        val offset = (-10).dp
        FloatingAddButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(offset.withLayoutDirection(), offset), openEditor)
    }
}

@Composable
fun ProductsList(viewModel: ProductViewModel, products: List<Product>,
                 openEditor: () -> Unit = {}, lazyListState: LazyListState) {
    val productsSorted = remember(products) {
        products.sortedWith (
            compareBy<Product> { it.name }
                .thenBy { it.production }
        )
    }

    LazyColumn (state = lazyListState,
        contentPadding = PaddingValues(bottom = 80.dp)) {
        items(productsSorted.size) { idx ->
            ProductCard(productsSorted[idx], Modifier.padding(vertical = 5.dp,
                horizontal = 5.dp), viewModel
            ) {
                viewModel.productObserved = productsSorted[idx]
                openEditor()
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, viewModel: ProductViewModel,
                openEditor: () -> Unit = {}) {
    CardSurface(modifier) {
        ProductCardContent(product)

        val width = remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Box(Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                width.value = with(density) { coordinates.size.width.toDp() }
            }, contentAlignment = Alignment.TopEnd) {
            val visibleState = remember {mutableStateOf(false)}

            EditMenu(visibleState, openEditor, {viewModel.removeProduct(product)},
                offset = DpOffset(width.value.withLayoutDirection(), 0.dp))
            IconButton({visibleState.value = true}) {
                Icon(Icons.Filled.MoreHoriz, "открыть меню редактирования")
            }
        }
    }
}

@Composable
fun ProductCardContent(product: Product) {
    Column(modifier = Modifier
        .background(Color.Transparent)
        .padding(10.dp)) {
        Text("Название: " + product.name)
        Text("Производитель: " + product.production)
        Nutrients(product, Modifier.padding(horizontal = 2.dp, vertical = 5.dp))
        Row(modifier = Modifier.align(Alignment.End)) {
            Text("Кол-во: ")
            Text("${product.amount} ${product.type.toStringWithLanguage()}")
        }
    }
}

@Composable
fun Nutrients(product: Product, modifier: Modifier = Modifier) {
    Column(modifier) {
        NutrientsHorizontalDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween) {
            NutrientsVerticalDivider()
            Nutrient("Ценность", product.calories, "ккал")
            NutrientsVerticalDivider()
            Nutrient("Б", product.protein, "грамм")
            NutrientsVerticalDivider()
            Nutrient("Ж", product.fat, "грамм")
            NutrientsVerticalDivider()
            Nutrient("У", product.carbohydrates, "грамм")
            NutrientsVerticalDivider()
            Nutrient("Волокна", product.fiber, "грамм")
            NutrientsVerticalDivider()
        }
        NutrientsHorizontalDivider()
    }
}

@Composable
fun Nutrient(description: String, value: Double, type: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(description)
        Text(value.toString())
        Text(type)
    }
}

@Composable
fun NutrientsHorizontalDivider() {HorizontalDivider(2.dp, Color.Gray)}

@Composable
fun NutrientsVerticalDivider() {VerticalDivider(2.dp, Color.Gray)}
