package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.presentation.viewModel.date.DateViewModel
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel
import ru.foodcare.foodcare.presentation.viewModel.weight.WeightViewModel

sealed class Route(val route: String) {
    object Main: Route("main")
    object Calendar: Route("calendar")
    object Products: Route("products")
    object ProductEditor: Route("productEditor")
    object MealEditor: Route("mealEditor")
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
fun Content(
    navPanelState: DrawerState,
    navController: NavHostController,
    productViewModel: ProductViewModel,
    mealViewModel: MealViewModel,
    dateVM: DateViewModel,
    weightVM: WeightViewModel
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val onMainWindow = remember {derivedStateOf {
        currentBackStackEntry.value?.destination?.route == Route.Main.route
    }}
    val productEditorIsOpened = remember {derivedStateOf {
        currentBackStackEntry.value?.destination?.route == Route.ProductEditor.route
    }}
    val productIsOpened = remember {derivedStateOf {
        currentBackStackEntry.value?.destination?.route == Route.Products.route
    }}
    val mealEditorIsOpened = remember {derivedStateOf {
        currentBackStackEntry.value?.destination?.route == Route.MealEditor.route
    }}

    Scaffold(topBar = {
        TopAppBar({
            if (onMainWindow.value) Text("Foodcare")
        }, navigationIcon = {
            val navPanelScope = rememberCoroutineScope()
            if (productEditorIsOpened.value || mealEditorIsOpened.value) {
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
                    SearchProductField(productViewModel, Modifier.widthIn(50.dp, 200.dp))
                }
            })
    }) {
        FoodcareNavHost(navController,
            productViewModel, mealViewModel, dateVM, weightVM,
            Modifier.padding(it))
    }
}

@Composable
fun FoodcareNavHost(
    navController: NavHostController, productViewModel: ProductViewModel,
    mealViewModel: MealViewModel,
    dateVM: DateViewModel,
    weightVM: WeightViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController, Route.Main.route, modifier) {
        composable(Route.Main.route) {
            MainWindow(mealViewModel, ) {
                navController.navigate(Route.MealEditor.route)
            }
        }
        composable(Route.Calendar.route) {
            CalendarWindow(mealViewModel, dateVM, weightVM) {
                navController.navigate(Route.MealEditor.route)
            }
        }
        composable(Route.Products.route) {
            ProductsWindow(productViewModel) {
                navController.navigate(Route.ProductEditor.route)
            }
        }
        composable(Route.ProductEditor.route) {
            ProductEditWindow(productViewModel) {navController.popBackStack()}
        }
        composable(Route.MealEditor.route) {
            MealEditWindow(mealViewModel, productViewModel) {navController.popBackStack()}
        }
    }
}