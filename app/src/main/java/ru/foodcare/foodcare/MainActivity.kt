package ru.foodcare.foodcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.foodcare.foodcare.ui.theme.FoodcareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodcareTheme {
                UI()
            }
        }
    }
}

sealed class Route(val route: String) {
    object Main: Route("main")
    object Calendar: Route("calendar")
    object Products: Route("products")
}

@Composable
fun UI() {
    val navPanelState = rememberDrawerState (DrawerValue.Open)
    val navigationController = rememberNavController()

    NavigationPanel(navPanelState, navigationController) {
        Content(navPanelState, navigationController)
    }
}

@Composable
fun NavigationPanel(navPanelState: DrawerState, navController: NavHostController, content: @Composable () -> Unit) {
    val drawerContent = @Composable {
        ModalDrawerSheet {
            NavigationDrawerItem({Text("Главная")},
                navController.currentDestination?.route == Route.Main.route,
                {navController.navigate(Route.Main.route)})
            NavigationDrawerItem({Text("Календарь")},
                navController.currentDestination?.route == Route.Calendar.route,
                {navController.navigate(Route.Calendar.route)})
            NavigationDrawerItem({Text("Продукты")},
                navController.currentDestination?.route == Route.Products.route,
                {navController.navigate(Route.Products.route)})
        }
    }

    ModalNavigationDrawer(drawerContent, drawerState = navPanelState) {
        content()
    }
}

@Composable
fun Content(navPanelState: DrawerState, navController: NavHostController) {

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FoodcareTheme {
        UI()
    }
}