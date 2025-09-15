package ru.foodcare.foodcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import ru.foodcare.foodcare.ui.composable.Content
import ru.foodcare.foodcare.ui.composable.NavigationPanel
import ru.foodcare.foodcare.ui.viewModel.date.DateViewModel
import ru.foodcare.foodcare.ui.viewModel.date.DateViewModelFactory
import ru.foodcare.foodcare.ui.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.ui.viewModel.meal.MealViewModelFactory
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModelFactory
import ru.foodcare.foodcare.ui.viewModel.weight.WeightViewModel
import ru.foodcare.foodcare.ui.viewModel.weight.WeightViewModelFactory
import ru.foodcare.foodcare.ui.theme.FoodcareTheme

class MainActivity : ComponentActivity() {

    private lateinit var productVM: ProductViewModel
    private lateinit var mealVM: MealViewModel
    private lateinit var dateVM: DateViewModel
    private lateinit var weightVM: WeightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val foodcareComponent = application.foodcare.foodcareComponent

        val productFactory = ProductViewModelFactory(
            foodcareComponent.getProductRepository(), Dispatchers.IO)
        val mealFactory = MealViewModelFactory(
            foodcareComponent.getMealRepository(), Dispatchers.IO)
        val dateFactory = DateViewModelFactory(
            foodcareComponent.getDateRepository(), Dispatchers.IO)
        val weightFactory = WeightViewModelFactory(
            foodcareComponent.getWeightRepository(), Dispatchers.IO)

        productVM = ViewModelProvider(this, productFactory)[ProductViewModel::class.java]
        mealVM = ViewModelProvider(this, mealFactory)[MealViewModel::class.java]
        dateVM = ViewModelProvider(this, dateFactory)[DateViewModel::class.java]
        weightVM = ViewModelProvider(this, weightFactory)[WeightViewModel::class.java]

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
