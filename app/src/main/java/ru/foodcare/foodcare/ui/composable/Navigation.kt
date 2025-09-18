package ru.foodcare.foodcare.ui.composable

import ru.foodcare.foodcare.R
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.ui.composable.meal.MealEditWindow
import ru.foodcare.foodcare.ui.composable.product.ProductEditWindow
import ru.foodcare.foodcare.ui.composable.product.ProductsWindow
import ru.foodcare.foodcare.ui.composable.product.SearchProductField
import ru.foodcare.foodcare.ui.composable.weight.WeightEditWindow
import ru.foodcare.foodcare.ui.viewModel.date.DateViewModel
import ru.foodcare.foodcare.ui.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel
import ru.foodcare.foodcare.ui.viewModel.weight.WeightViewModel

sealed class Route(val route: String) {
    object Main: Route("main")
    object Calendar: Route("calendar")
    object Products: Route("products")
    object ProductEditor: Route("productEditor")
    object MealEditor: Route("mealEditor")
    object WeightEditor: Route("weightEditor")
}

@Composable
fun NavigationPanel(navPanelState: DrawerState, navController: NavHostController, content: @Composable () -> Unit) {
    val drawerContent = @Composable {
        ModalDrawerSheet {
            val navPanelScope = rememberCoroutineScope()
            val closeNavPanel = {navPanelScope.launch { navPanelState.close() }}
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            val startRoute = Route.Main.route

            NavigationDrawerItem({Text(stringResource(R.string.main_page))},
                currentRoute == Route.Main.route, {
                    navController.navigateWithPopTo(Route.Main.route, startRoute)
                    closeNavPanel()
                })
            NavigationDrawerItem({Text(stringResource(R.string.calendar))},
                currentRoute == Route.Calendar.route, {
                    navController.navigateWithPopTo(Route.Calendar.route, startRoute)
                    closeNavPanel()
                })
            NavigationDrawerItem({Text(stringResource(R.string.products))},
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
    val productIsOpened = remember {derivedStateOf {
        currentBackStackEntry.value?.destination?.route == Route.Products.route
    }}

    val arrowBackIsNeeded by remember {
        derivedStateOf {
            when (currentBackStackEntry.value?.destination?.route) {
                Route.WeightEditor.route -> true
                Route.MealEditor.route -> true
                Route.ProductEditor.route -> true
                else -> false
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        TopAppBar({
            if (onMainWindow.value) Text(stringResource(R.string.app_name))
        }, navigationIcon = {
            val navPanelScope = rememberCoroutineScope()
            if (arrowBackIsNeeded) {
                IconButton({navController.popBackStack()}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack,
                        stringResource(R.string.exit_the_editor))
                }
            } else {
                IconButton({
                    navPanelScope.launch { navPanelState.open() }
                }) { Icon(Icons.Default.Menu, stringResource(R.string.menu)) }
            }
        },
            actions = {
                if (productIsOpened.value) {
                    SearchProductField(productViewModel, Modifier.widthIn(50.dp, 200.dp))
                }
            })
    },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            CommonSnackbar(data)
        } }
    ) {
        FoodcareNavHost(navController, snackbarHostState,
            productViewModel, mealViewModel, dateVM, weightVM,
            Modifier.padding(it))
    }
}

@Composable
fun FoodcareNavHost(
    navController: NavHostController, snackbarHostState: SnackbarHostState,
    productViewModel: ProductViewModel,
    mealViewModel: MealViewModel,
    dateVM: DateViewModel,
    weightVM: WeightViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController, Route.Main.route, modifier) {
        composable(Route.Main.route) {
            MainWindow(mealViewModel, weightVM, snackbarHostState,
                openMealEditor = {
                    navController.navigate(Route.MealEditor.route)
                },
                openWeightEditor = {
                    navController.navigate(Route.WeightEditor.route)
                }
            )
        }
        composable(Route.Calendar.route) {
            CalendarWindow(
                mealViewModel, dateVM, weightVM, snackbarHostState,
                openMealEditor = {
                    navController.navigate(Route.MealEditor.route)
                },
                openWeightEditor = {
                    navController.navigate(Route.WeightEditor.route)
                }
            )
        }
        composable(Route.Products.route) {
            ProductsWindow(productViewModel, snackbarHostState) {
                navController.navigate(Route.ProductEditor.route)
            }
        }
        composable(Route.ProductEditor.route) {
            ProductEditWindow(productViewModel) { navController.popBackStack() }
        }
        composable(Route.MealEditor.route) {
            MealEditWindow(mealViewModel, productViewModel) { navController.popBackStack() }
        }
        composable(Route.WeightEditor.route) {
            WeightEditWindow(weightVM) { navController.popBackStack() }
        }
    }
}