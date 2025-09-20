package ru.foodcare.foodcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import ru.foodcare.foodcare.ui.composable.Content
import ru.foodcare.foodcare.ui.composable.NavigationPanel
import ru.foodcare.foodcare.ui.viewModel.date.DateViewModel
import ru.foodcare.foodcare.ui.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel
import ru.foodcare.foodcare.ui.viewModel.weight.WeightViewModel
import ru.foodcare.foodcare.ui.theme.FoodcareTheme

class MainActivity : ComponentActivity() {
    private lateinit var productVM: ProductViewModel
    private lateinit var mealVM: MealViewModel
    private lateinit var dateVM: DateViewModel
    private lateinit var weightVM: WeightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val foodcareComponent = application.foodcare.foodcareComponent
        val mainActivityComponent = foodcareComponent.getMainActivityComponentFactory().create()
        val viewModelFactory = mainActivityComponent.getViewModelFactory()

        productVM = ViewModelProvider(this, viewModelFactory)[ProductViewModel::class.java]
        mealVM = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]
        dateVM = ViewModelProvider(this, viewModelFactory)[DateViewModel::class.java]
        weightVM = ViewModelProvider(this, viewModelFactory)[WeightViewModel::class.java]

        setContent {
            FoodcareTheme {
                UI(productVM, mealVM, dateVM, weightVM)
            }
        }
    }
}

@Composable
fun UI(productViewModel: ProductViewModel,
       mealViewModel: MealViewModel,
       dateVM: DateViewModel,
       weightVM: WeightViewModel) {
    val navPanelState = rememberDrawerState (DrawerValue.Closed)
    val navigationController = rememberNavController()

    NavigationPanel(navPanelState, navigationController) {
        Content(navPanelState, navigationController,
            productViewModel, mealViewModel, dateVM, weightVM)
    }
}
