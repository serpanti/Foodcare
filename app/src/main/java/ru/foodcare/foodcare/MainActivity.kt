package ru.foodcare.foodcare

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.data.database.FoodcareDBProvider
import ru.foodcare.foodcare.data.product.ProductRepositoryImpl
import ru.foodcare.foodcare.domain.Product
import ru.foodcare.foodcare.presentation.mock.MockProductViewModel
import ru.foodcare.foodcare.presentation.viewModel.ProductViewModel
import ru.foodcare.foodcare.presentation.viewModel.ProductViewModelFactory
import ru.foodcare.foodcare.ui.theme.FoodcareTheme

class MainActivity : ComponentActivity() {

    private lateinit var productVM: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = FoodcareDBProvider.getInstance(applicationContext)
        val repository = ProductRepositoryImpl(db.productDAO())
        val factory = ProductViewModelFactory(repository, Dispatchers.IO)
        productVM = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        setContent {
            FoodcareTheme {
                UI(productVM)
            }
        }
    }
}

sealed class Route(val route: String) {
    object Main: Route("main")
    object Calendar: Route("calendar")
    object Products: Route("products")
    object ProductEditor: Route("productEditor")
}

@Composable
fun UI(productViewModel: ProductViewModel) {
    val navPanelState = rememberDrawerState (DrawerValue.Closed)
    val navigationController = rememberNavController()

    NavigationPanel(navPanelState, navigationController) {
        Content(navPanelState, navigationController, productViewModel)
    }
}

@Composable
fun NavigationPanel(navPanelState: DrawerState, navController: NavHostController, content: @Composable () -> Unit) {
    val drawerContent = @Composable {
        ModalDrawerSheet {
            val buildNavOptions: NavOptionsBuilder.() -> Unit = {
                launchSingleTop = true
            }
            val navPanelScope = rememberCoroutineScope()
            val closeNavPanel = {navPanelScope.launch { navPanelState.close() }}
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            NavigationDrawerItem({Text("Главная")},
                currentRoute == Route.Main.route, {
                    navController.navigate(Route.Main.route, buildNavOptions)
                    closeNavPanel()
                })
            NavigationDrawerItem({Text("Календарь")},
                currentRoute == Route.Calendar.route, {
                    navController.navigate(Route.Calendar.route, buildNavOptions)
                    closeNavPanel()
                })
            NavigationDrawerItem({Text("Продукты")},
                currentRoute == Route.Products.route || currentRoute == Route.ProductEditor.route, {
                    if (false) {
                        navController.navigate(Route.ProductEditor.route) {
                            popUpTo(Route.ProductEditor.route)
                        }
                    } else {
                        navController.navigate(Route.Products.route, buildNavOptions)
                    }

                    closeNavPanel()
                }
            )
        }
    }

    ModalNavigationDrawer(drawerContent, drawerState = navPanelState) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(navPanelState: DrawerState, navController: NavHostController, productViewModel: ProductViewModel) {
    Scaffold(topBar = {
        TopAppBar({
            Text("Foodcare")
        }, navigationIcon =  {
            val navPanelScope = rememberCoroutineScope()

            IconButton({
                navPanelScope.launch { navPanelState.open() }
            }) { Icon(Icons.Default.Menu, "Меню") }
        })
    }) {
        NavHost(navController, Route.Products.route, Modifier.padding(it)) {
            composable(Route.Main.route) {}
            composable(Route.Calendar.route) {}
            composable(Route.Products.route) {ProductsWindow(productViewModel, navController)}
            composable(Route.ProductEditor.route) {
                ProductEditWindow(productViewModel, navController)
            }
        }
    }
}

@Composable
fun ProductsWindow(viewModel: ProductViewModel, navController: NavHostController) {
    val products by viewModel.products.collectAsState()
    val openProductEditor: () -> Unit = {
        navController.navigate(Route.ProductEditor.route) {launchSingleTop = true}
    }

    Products(viewModel, products, openProductEditor)
}

@Composable
fun Products(viewModel: ProductViewModel, products: List<Product>,
             openEditor: () -> Unit = {}) {
    val productsSorted = remember(products) {
        products.sortedWith (
            compareBy<Product> { it.name }
                .thenBy { it.production }
        )
    }

    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn (state = lazyListState) {
            items(productsSorted.size) { idx ->
                ProductCard(productsSorted[idx], Modifier.padding(vertical = 5.dp,
                    horizontal = 5.dp), viewModel
                ) {
                    viewModel.productObserved = productsSorted[idx]
                    openEditor()
                }
            }
        }

        val showButton by remember {
            derivedStateOf { lazyListState.firstVisibleItemIndex >= 4 }
        }
        AnimatedVisibility(visible = showButton,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically() + fadeIn(), exit = slideOutVertically() + fadeOut()) {
            IconButton({
                scope.launch {
                    lazyListState.scrollToItem(0)
                }
            }) { Icon(Icons.Filled.KeyboardArrowUp, "Переместиться наверх") }
        }
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, viewModel: ProductViewModel,
                openEditor: () -> Unit = {}) {
    Surface(modifier = modifier, shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color.Black), shadowElevation = 5.dp) {
        ProductCardContent(product)

        val width = remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Box(Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
            width.value = with(density) {coordinates.size.width.toDp()}
        }, contentAlignment = Alignment.TopEnd) {
            val visibleState = remember {mutableStateOf(false)}

            EditMenu(visibleState, openEditor, {viewModel.removeProduct(product)},
                offset = DpOffset(width.value, 0.dp))
            IconButton({visibleState.value = true}) {
                Icon(Icons.Filled.MoreHoriz, "открыть меню редактирования")
            }
        }
    }
}

@Composable
fun ProductCardContent(product: Product) {
    Column(modifier = Modifier.background(Color.Transparent).padding(10.dp)) {
        Text("Название: " + product.name)
        Text("Производитель: " + product.production)
        Nutrients(product, Modifier.padding(horizontal = 2.dp, vertical = 5.dp))
        Row(modifier = Modifier.align(Alignment.End)) {
            Text("Кол-во: ")
            val countTypeString = when (product.type) {
                Product.Companion.UnitType.Milliliter -> "мл"
                Product.Companion.UnitType.Gram -> "г"
                Product.Companion.UnitType.Piece -> "шт"
            }
            Text("${product.amount} $countTypeString")
        }
    }
}

@Composable
fun EditMenu(visibleState: MutableState<Boolean>,
             edit: () -> Unit, remove: () -> Unit, modifier: Modifier = Modifier, offset: DpOffset) {
    DropdownMenu(visibleState.value, {visibleState.value = false}, modifier = modifier,
        offset = offset) {
        DropdownMenuItem({
            Text("Редактировать")
        }, {
            visibleState.value = false
            edit()
        }, trailingIcon = {Icon(Icons.Filled.Edit, null)})
        HorizontalDivider(1.dp, Color.Gray)
        DropdownMenuItem({
            Text("Удалить")
        }, {
            visibleState.value = false
            remove()
        }, trailingIcon = {Icon(Icons.Filled.Delete, null)})
    }
}

@Composable
fun Nutrients(product: Product, modifier: Modifier = Modifier) {
    Column(modifier) {
        NutrientsHorizontalDivider()
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
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

@Composable
fun VerticalDivider(width: Dp, color: Color) {
    Box(modifier = Modifier.width(width).fillMaxHeight().background(color))
}

@Composable
fun HorizontalDivider(height: Dp, color: Color) {
    Box(modifier = Modifier.height(height).fillMaxWidth().background(color))
}

@Composable
fun ProductEditWindow(viewModel: ProductViewModel, navController: NavHostController) {
    // TODO: написать редактор
    viewModel.productObserved?.let {
        ProductCard(it, viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FoodcareTheme {
        @SuppressLint("ViewModelConstructorInComposable")
        UI(MockProductViewModel())
    }
}
