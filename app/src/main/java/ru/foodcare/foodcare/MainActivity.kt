package ru.foodcare.foodcare

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import ru.foodcare.foodcare.data.database.FoodcareDBProvider
import ru.foodcare.foodcare.data.meal.MealRepositoryImpl
import ru.foodcare.foodcare.data.product.ProductRepositoryImpl
import ru.foodcare.foodcare.presentation.composable.Content
import ru.foodcare.foodcare.presentation.composable.NavigationPanel
import ru.foodcare.foodcare.presentation.mock.MockMealViewModel
import ru.foodcare.foodcare.presentation.mock.MockProductViewModel
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModelFactory
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModelFactory
import ru.foodcare.foodcare.ui.theme.FoodcareTheme

class MainActivity : ComponentActivity() {

    private lateinit var productVM: ProductViewModel
    private lateinit var mealVM: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = FoodcareDBProvider.getInstance(applicationContext)
        val productRepository = ProductRepositoryImpl(db.productDAO())
        val mealRepository = MealRepositoryImpl(db.mealDAO(), db.productDAO(), db.dateDAO())
        val productFactory = ProductViewModelFactory(productRepository, Dispatchers.IO)
        val mealFactory = MealViewModelFactory(mealRepository, Dispatchers.IO)
        productVM = ViewModelProvider(this, productFactory)[ProductViewModel::class.java]
        mealVM = ViewModelProvider(this, mealFactory)[MealViewModel::class.java]

        setContent {
            FoodcareTheme {
                UI(productVM, mealVM)
            }
        }
    }
}

@Composable
fun UI(productViewModel: ProductViewModel, mealViewModel: MealViewModel) {
    val navPanelState = rememberDrawerState (DrawerValue.Closed)
    val navigationController = rememberNavController()

    NavigationPanel(navPanelState, navigationController) {
        Content(navPanelState, navigationController, productViewModel, mealViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FoodcareTheme {
        @SuppressLint("ViewModelConstructorInComposable")
        UI(MockProductViewModel(), MockMealViewModel())
    }
}
