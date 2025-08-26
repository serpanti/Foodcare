package ru.foodcare.foodcare

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.data.database.FoodcareDBProvider
import ru.foodcare.foodcare.data.product.ProductRepositoryImpl
import ru.foodcare.foodcare.domain.Product
import ru.foodcare.foodcare.domain.Product.Companion.UnitType
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
            val navPanelScope = rememberCoroutineScope()
            val closeNavPanel = {navPanelScope.launch { navPanelState.close() }}
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            val startRoute = Route.Main.route

            NavigationDrawerItem({Text("Главная")},
                currentRoute == Route.Main.route, {
                    navController.navigateWithPopTo(Route.Main.route, startRoute)
                    closeNavPanel()
                })
            NavigationDrawerItem({Text("Календарь")},
                currentRoute == Route.Calendar.route, {
                    navController.navigateWithPopTo(Route.Calendar.route, startRoute)
                    closeNavPanel()
                })
            NavigationDrawerItem({Text("Продукты")},
                currentRoute == Route.Products.route, {
                    navController.navigateWithPopTo(Route.Products.route, startRoute)
                    closeNavPanel()
                }
            )
        }
    }

    ModalNavigationDrawer(drawerContent, drawerState = navPanelState) {
        content()
    }
}

private fun NavHostController.navigateWithPopTo(route: String, startRoute: String) {
    navigate(route) {
        popUpTo(startRoute) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(navPanelState: DrawerState, navController: NavHostController, productViewModel: ProductViewModel) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val productEditorIsOpened = remember {derivedStateOf {
        currentBackStackEntry.value?.destination?.route == Route.ProductEditor.route
    }}
    val productIsOpened = remember {derivedStateOf {
        currentBackStackEntry.value?.destination?.route == Route.Products.route
    }}

    Scaffold(topBar = {
        TopAppBar({
            Text("Foodcare")
        }, navigationIcon = {
            val navPanelScope = rememberCoroutineScope()
            if (productEditorIsOpened.value) {
                IconButton({navController.popBackStack()}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "выйти из редактора")
                }
            } else {
                IconButton({
                    navPanelScope.launch { navPanelState.open() }
                }) { Icon(Icons.Default.Menu, "Меню") }
            }
        },
            actions = {
                if (productIsOpened.value) {
                    SearchProductField(productViewModel)
                }
            })
    }) {
        FoodcareNavHost(navController, productViewModel, Modifier.padding(it))
    }
}

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
fun SearchField(onStartSearching: () -> Unit,
                onStopSearching: () -> Unit,
                startValue: String,
                onValueChange: (String) -> Unit,
                isOpened: () -> Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.offset(y = 2.dp).padding(5.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(15.dp)).height(55.dp)
            .padding(5.dp)) {
        if (isOpened()) {
            SearchTextField(startValue, onValueChange)
        }
        IconButton({
            if (isOpened()) onStopSearching() else onStartSearching()
        }) {
            if (isOpened()) {
                Icon(Icons.Filled.Close, "Закрыть поиск")
            } else {
                Icon(Icons.Filled.Search, "Открыть поиск")
            }
        }
    }
}

@Composable
fun SearchTextField(startText: String, onValueChange: (String) -> Unit,
                     modifier: Modifier = Modifier) {
    SimpleTextField(startText, onValueChange,
        placeholder = {Text("Поиск")},
        modifier = modifier.width(150.dp).padding(start = 5.dp)
    )
}

@Composable
fun SimpleTextField(value: String, onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier,
                    placeholder: @Composable () -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        decorationBox = @Composable { innerTextField ->
        if (value.isEmpty()) {
                placeholder()
            } else {
                innerTextField()
            }
        }
    )
}

@Composable
fun FoodcareNavHost(navController: NavHostController, productViewModel: ProductViewModel,
    modifier: Modifier = Modifier) {
    NavHost(navController, Route.Main.route, modifier) {
        composable(Route.Main.route) {}
        composable(Route.Calendar.route) {}
        composable(Route.Products.route) {
            ProductsWindow(productViewModel) {
                navController.navigate(Route.ProductEditor.route)
            }
        }
        composable(Route.ProductEditor.route) {
            ProductEditWindow(productViewModel) {navController.popBackStack()}
        }
    }
}

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
        ProductsLoading()
    } else {
        products.value?.let {
            Products(viewModel, it, openProductEditor)
        }
    }
}

@Composable
fun ProductsLoading() {
    var indicatorSize by remember {mutableStateOf(40.dp)}
    val density = LocalDensity.current

    Box(modifier = Modifier.fillMaxSize()
        .padding(70.dp)
        .onGloballyPositioned {coordinates ->
            with(density) {
                val minSize = minOf(coordinates.size.width, coordinates.size.height).toDp()
                if (minSize != indicatorSize) indicatorSize = minSize
            }
        },
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier
            .size(indicatorSize), strokeWidth = 20.dp)
    }
}

@Composable
fun Products(viewModel: ProductViewModel, products: List<Product>,
             openEditor: () -> Unit = {}, lazyListState: LazyListState = rememberLazyListState()) {

    Box(modifier = Modifier.fillMaxSize()) {
        ProductsList(viewModel, products, openEditor, lazyListState)

        SwipeToStartButton(modifier = Modifier.align(Alignment.BottomCenter), lazyListState)

        AddProductButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset((-10).dp, (-10).dp), openEditor)
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
fun SwipeToStartButton(modifier: Modifier = Modifier, lazyListState: LazyListState) {
    val scope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 4 }
    }

    AnimatedVisibility(visible = showButton,
        modifier = modifier,
        enter = slideInVertically() + fadeIn(), exit = slideOutVertically() + fadeOut()) {
        IconButton({
            scope.launch {
                lazyListState.scrollToItem(0)
            }
        }) { Icon(Icons.Filled.KeyboardArrowUp, "Переместиться наверх") }
    }
}

@Composable
fun AddProductButton(modifier: Modifier = Modifier, openEditor: () -> Unit = {}) {
    FloatingActionButton(openEditor,
        shape = CircleShape,
        modifier = modifier,
        containerColor = Color.LightGray) {
        Icon(Icons.Filled.AddCircle, "открыть меню добавления")
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, viewModel: ProductViewModel,
                openEditor: () -> Unit = {}) {
    ProductCardSurface(modifier) {
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
                offset = DpOffset(width.value, 0.dp))
            IconButton({visibleState.value = true}) {
                Icon(Icons.Filled.MoreHoriz, "открыть меню редактирования")
            }
        }
    }
}

@Composable
fun ProductCardSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier, shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color.Black), shadowElevation = 5.dp) {
        content()
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
            val countTypeString = when (product.type) {
                UnitType.Milliliter -> "мл"
                UnitType.Gram -> "г"
                UnitType.Piece -> "шт"
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

@Composable
fun VerticalDivider(width: Dp, color: Color) {
    Box(modifier = Modifier
        .width(width)
        .fillMaxHeight()
        .background(color))
}

@Composable
fun HorizontalDivider(height: Dp, color: Color) {
    Box(modifier = Modifier
        .height(height)
        .fillMaxWidth()
        .background(color))
}

@Composable
fun ProductEditWindow(viewModel: ProductViewModel, close: () -> Unit) {
    val product = remember {viewModel.productObserved}

    ProductCardSurface(Modifier
        .fillMaxSize()
        .padding(bottom = 15.dp)
        .padding(5.dp)
        .background(Color.Transparent)
        .padding(10.dp)) {
        ProductEditCard(product, viewModel, close)
    }
}

@Composable
fun ProductEditCard(oldProduct: Product?, viewModel: ProductViewModel, close: () -> Unit) {
    val name = remember { mutableStateOf(oldProduct?.name ?: "") }
    val production = remember { mutableStateOf(oldProduct?.production ?: "") }
    val type = remember { mutableStateOf(UnitType.Piece) }
    val calories = remember { mutableStateOf(oldProduct?.calories?.toString() ?: "") }
    val protein = remember { mutableStateOf(oldProduct?.protein?.toString() ?: "") }
    val fat = remember { mutableStateOf(oldProduct?.fat?.toString() ?: "") }
    val carbohydrates = remember { mutableStateOf(oldProduct?.carbohydrates?.toString() ?: "") }
    val fiber = remember { mutableStateOf(oldProduct?.fiber?.toString() ?: "") }

    LazyColumn {
        itemWithUnderLine {NameEdit(name)}
        itemWithUnderLine {ProductionEdit(production)}
        itemWithUnderLine {CaloriesEdit(calories)}
        itemWithUnderLine {ProteinEdit(protein)}
        itemWithUnderLine {FatEdit(fat)}
        itemWithUnderLine {CarbohydratesEdit(carbohydrates)}
        itemWithUnderLine {FiberEdit(fiber)}
        itemWithUnderLine {ProductTypeSelector(type)}
        item {
            val caloriesNum = parseOrNull(calories.value)
            val proteinNum = parseOrNull(protein.value)
            val fatNum = parseOrNull(fat.value)
            val carbohydratesNum = parseOrNull(carbohydrates.value)
            val fiberNum = parseOrNull(fiber.value)

            SaveProductButton(oldProduct, InputProduct(name.value, production.value,
                if (type.value == UnitType.Piece) 1 else 100, type.value,
                caloriesNum, proteinNum, fatNum, carbohydratesNum,
                fiberNum),
                viewModel, close)
        }
    }
}

class InputProduct(var name: String = "",
                           var production: String  = "",
                           var amount: Int = 1,
                           var type: UnitType = UnitType.Piece,
                           var calories: Double? = null,
                           var protein: Double? = null,
                           var fat: Double? = null,
                           var carbohydrates: Double? = null,
                           var fiber: Double? = null) {
    fun isCorrect(): Boolean {
        return (calories != null && protein != null && fat != null &&
                carbohydrates != null && fiber != null &&
                name.isNotEmpty() && production.isNotEmpty())
    }
    
    fun toProduct(): Product {
        return Product(name, production, amount, type,
            calories ?: 0.0, protein ?: 0.0, fat ?: 0.0, carbohydrates ?: 0.0, fiber ?: 0.0)
    }
}

private fun parseOrNull(str: String): Double? {
    return str.replace(',', '.').trim().toDoubleOrNull()
}

@Composable
fun NameEdit(name: MutableState<String>) {
    ProductTextField(name.value, {name.value = it},
        prefix = {Text("Название: ")},
        placeholder = {Text("Название продукта")})
}

@Composable
fun ProductionEdit(production: MutableState<String>) {
    ProductTextField(production.value, {production.value = it},
        prefix = {Text("Производитель: ")},
        placeholder = {Text("Имя производителя")})
}

@Composable
fun CaloriesEdit(calories: MutableState<String>) {
    NutrientTextField("Калорийность: ", calories.value, "ккал"
    ) { calories.value = it }
}

@Composable
fun ProteinEdit(protein: MutableState<String>) {
    NutrientTextField("Белок: ", protein.value, "грамм"
    ) { protein.value = it }
}

@Composable
fun FatEdit(fat: MutableState<String>) {
    NutrientTextField("Жир: ", fat.value, "грамм"
    ) { fat.value = it }
}

@Composable
fun CarbohydratesEdit(carbohydrates: MutableState<String>) {
    NutrientTextField("Углеводы: ", carbohydrates.value, "грамм"
    ) { carbohydrates.value = it }
}

@Composable
fun FiberEdit(fiber: MutableState<String>) {
    NutrientTextField("Волокна: ", fiber.value, "грамм"
    ) { fiber.value = it }
}

private fun LazyListScope.itemWithUnderLine(content: @Composable (LazyItemScope.() -> Unit)) {
    item {
        content()
        HorizontalDivider(2.dp, Color.Black)
    }
}

@Composable
fun SaveProductButton(
    oldProduct: Product?,
    inputProduct: InputProduct,
    viewModel: ProductViewModel,
    close: () -> Unit
) {
    var showAlert by remember { mutableStateOf(false) }
    if (showAlert) {
        AlertWrongInput ({ WrongInputProductPreview(inputProduct) }) { showAlert = false }
    }
    TextButton({
        if (!inputProduct.isCorrect()) {
            showAlert = true
        } else {
            if (oldProduct != null) {
                viewModel.updateProduct(inputProduct.toProduct(), oldProduct)
            } else {
                viewModel.addProduct(inputProduct.toProduct())
            }
            close()
        }
    }, modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
        colors = ButtonColors(Color.Transparent, Color.Black,
            Color.Transparent, Color.Transparent)) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Save, "")
            Text("Сохранить", fontSize = 20.sp)
        }
    }
}

@Composable
fun WrongInputProductPreview(inputProduct: InputProduct) {
    Column {
        if (inputProduct.name.isEmpty()) Text("Имя не указано")
        if (inputProduct.production.isEmpty()) Text("Производитель не указан")
        if (inputProduct.calories == null) Text("Ошибка в калориях")
        if (inputProduct.protein == null) Text("Ошибка в белках")
        if (inputProduct.fat == null) Text("Ошибка в жирах")
        if (inputProduct.carbohydrates == null) Text("Ошибка в углеводах")
        if (inputProduct.fiber == null) Text("Ошибка в волокнах")
    }
}

@Composable
fun AlertWrongInput(preview: @Composable (() -> Unit) = {}, closeAlert: () -> Unit) {
    AlertDialog(closeAlert,
        {TextButton(closeAlert) {Text("Исправлю")} },
        modifier = Modifier.fillMaxWidth(),
        title = {Text("В вводе ошибки")},
        text = preview)
}

@Composable
fun ProductTypeSelector(type: MutableState<UnitType>) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 7.dp, vertical = 5.dp)
        .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
        .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        Text("Кол-во:")
        Column (verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(90.dp)) {
            ProductTypeSelectRow("1 шт - на 1 предмет", type.value == UnitType.Piece) {
                type.value = UnitType.Piece
            }
            ProductTypeSelectRow("100 г - на 100 грамм", type.value == UnitType.Gram) {
                type.value = UnitType.Gram
            }
            ProductTypeSelectRow("100 мл - на 0.1 литра", type.value == UnitType.Milliliter) {
                type.value = UnitType.Milliliter
            }
        }
    }
}

@Composable
fun ProductTypeSelectRow(type: String, selected: Boolean, onSelect: () -> Unit) {
    Row (modifier = Modifier
        .padding(start = 50.dp)
        .fillMaxWidth()
        .selectable(selected) {
            onSelect()
        }) {
        CheckCircle(selected)
        Text(type)
    }
}

@Composable
fun CheckCircle(selected: Boolean) {
    Crossfade (selected) { isSelected ->
        Icon(if (isSelected) {
            Icons.Filled.RadioButtonChecked
        } else {
            Icons.Filled.RadioButtonUnchecked
        }, "")
    }
}

@Composable
fun NutrientTextField(description: String, value: String, type: String,
                      onValueChange: (String) -> Unit) {
    ProductTextField((value), onValueChange,
        placeholder = {Text("Позволены символы: [0-9 и .]")},
        prefix = {Text(description)},
        suffix = {Text(type)})
}

@Composable
fun ProductTextField(startText: String, onValueChange: (String) -> Unit,
                     modifier: Modifier = Modifier,
                     placeholder: @Composable () -> Unit = {},
                     prefix: @Composable () -> Unit = {},
                     suffix: @Composable () -> Unit = {}) {
    TextField(startText, onValueChange,
        placeholder = placeholder,
        prefix = prefix,
        suffix = suffix,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FoodcareTheme {
        @SuppressLint("ViewModelConstructorInComposable")
        UI(MockProductViewModel())
    }
}
