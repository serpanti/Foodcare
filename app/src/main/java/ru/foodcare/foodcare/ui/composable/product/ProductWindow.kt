package ru.foodcare.foodcare.ui.composable.product

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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.ui.composable.AlertDeleteDialog
import ru.foodcare.foodcare.ui.composable.CardSurface
import ru.foodcare.foodcare.ui.composable.EditMenu
import ru.foodcare.foodcare.ui.composable.FloatingAddButton
import ru.foodcare.foodcare.ui.composable.HorizontalDivider
import ru.foodcare.foodcare.ui.composable.SwipeToStartButton
import ru.foodcare.foodcare.ui.composable.VerticalDivider
import ru.foodcare.foodcare.ui.composable.toStringWithLanguage
import ru.foodcare.foodcare.ui.composable.withLayoutDirection
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel

@Composable
fun ProductsWindow(
    viewModel: ProductViewModel,
    snackbarHostState: SnackbarHostState? = null,
    openProductEditor: () -> Unit
) {
    var products = if (viewModel.key.collectAsState().value == "") {
        viewModel.products
    } else {
        viewModel.productsByQuery
    }.collectAsState()

    Products(viewModel, products.value, snackbarHostState, openProductEditor)
}

@Composable
fun Products(viewModel: ProductViewModel, products: List<Product>,
             snackbarHostState: SnackbarHostState? = null,
             openEditor: () -> Unit = {},
             lazyListState: LazyListState = rememberLazyListState()) {

    Box(modifier = Modifier.fillMaxSize()) {
        ProductsList(viewModel, products, snackbarHostState, openEditor, lazyListState)

        SwipeToStartButton(modifier = Modifier.align(Alignment.BottomCenter), lazyListState)

        val offset = (-10).dp
        FloatingAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(offset.withLayoutDirection(), offset)
        ) {
            viewModel.onAddProduct()
            openEditor()
        }
    }
}

@Composable
fun ProductsList(
    viewModel: ProductViewModel, products: List<Product>,
    snackbarHostState: SnackbarHostState? = null,
    openEditor: () -> Unit = {}, lazyListState: LazyListState
) {
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
                horizontal = 5.dp), snackbarHostState, viewModel
            ) {
                viewModel.onUpdateProduct(productsSorted[idx])
                openEditor()
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product, modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    viewModel: ProductViewModel,
    openEditor: () -> Unit = {}
) {
    CardSurface(modifier) {
        ProductCardContent(product)

        val width = remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Box(
            Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    width.value = with(density) { coordinates.size.width.toDp() }
                }, contentAlignment = Alignment.TopEnd
        ) {
            val visibleState = remember { mutableStateOf(false) }
            var showAlert = remember { mutableStateOf(false) }

            DeleteProductContent(product, snackbarHostState, viewModel, showAlert)
            EditMenu(
                visibleState, openEditor, { showAlert.value = true },
                offset = DpOffset(width.value.withLayoutDirection(), 0.dp)
            )
            IconButton({ visibleState.value = true }) {
                Icon(Icons.Filled.MoreHoriz, "открыть меню редактирования")
            }
        }
    }
}

@Composable
fun DeleteProductContent(
    product: Product,
    snackbarHostState: SnackbarHostState? = null,
    viewModel: ProductViewModel,
    showAlert: MutableState<Boolean>
) {

    val deleteScope = rememberCoroutineScope()
    val removeProductAction = {viewModel.removeProduct(product)}
    val deleteAction: () -> Unit = {
        if (snackbarHostState != null) {
            deleteScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Скоро произойдет удаление",
                    actionLabel = "Отмена",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.Dismissed) removeProductAction()
            }
        } else {
            removeProductAction()
        }
    }

    if (showAlert.value) {
        AlertDeleteProductDialog({ showAlert.value = false }, product, delete = deleteAction)
    }
}

@Composable
fun AlertDeleteProductDialog(close: () -> Unit, product: Product, delete: () -> Unit) =
    AlertDeleteDialog(close, delete) {
        ProductCardContent(product)
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
fun NutrientsHorizontalDivider() {
    HorizontalDivider(2.dp, Color.Gray)
}

@Composable
fun NutrientsVerticalDivider() {
    VerticalDivider(2.dp, Color.Gray)
}
