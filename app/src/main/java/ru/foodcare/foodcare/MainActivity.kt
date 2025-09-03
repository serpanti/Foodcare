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
import ru.foodcare.foodcare.data.database.FoodcareDBProvider
import ru.foodcare.foodcare.data.date.DateRepositoryImpl
import ru.foodcare.foodcare.data.meal.MealRepositoryImpl
import ru.foodcare.foodcare.data.product.ProductRepositoryImpl
import ru.foodcare.foodcare.data.weightMeasurement.WeightRepositoryImpl
import ru.foodcare.foodcare.presentation.composable.Content
import ru.foodcare.foodcare.presentation.composable.NavigationPanel
import ru.foodcare.foodcare.presentation.viewModel.date.DateViewModel
import ru.foodcare.foodcare.presentation.viewModel.date.DateViewModelFactory
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModelFactory
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModelFactory
import ru.foodcare.foodcare.presentation.viewModel.weight.WeightViewModel
import ru.foodcare.foodcare.presentation.viewModel.weight.WeightViewModelFactory
import ru.foodcare.foodcare.ui.theme.FoodcareTheme

class MainActivity : ComponentActivity() {

    private lateinit var productVM: ProductViewModel
    private lateinit var mealVM: MealViewModel
    private lateinit var dateVM: DateViewModel
    private lateinit var weightVM: WeightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = FoodcareDBProvider.getInstance(applicationContext)

        val productRepository = ProductRepositoryImpl(db.productDAO())
        val mealRepository = MealRepositoryImpl(db.mealDAO(), db.productDAO(), db.dateDAO())
        val dateRepository = DateRepositoryImpl(db.dateDAO())
        val weightRepository = WeightRepositoryImpl(db.weightMeasurementDAO(), db.dateDAO())

        val productFactory = ProductViewModelFactory(productRepository, Dispatchers.IO)
        val mealFactory = MealViewModelFactory(mealRepository, Dispatchers.IO)
        val dateFactory = DateViewModelFactory(dateRepository, Dispatchers.IO)
        val weightFactory = WeightViewModelFactory(weightRepository, Dispatchers.IO)

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
