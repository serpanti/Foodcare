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
import ru.foodcare.foodcare.data.product.ProductRepositoryImpl
import ru.foodcare.foodcare.presentation.composable.Content
import ru.foodcare.foodcare.presentation.composable.NavigationPanel
import ru.foodcare.foodcare.presentation.mock.MockProductViewModel
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModelFactory
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

@Composable
fun UI(productViewModel: ProductViewModel) {
    val navPanelState = rememberDrawerState (DrawerValue.Closed)
    val navigationController = rememberNavController()

    NavigationPanel(navPanelState, navigationController) {
        Content(navPanelState, navigationController, productViewModel)
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
