package ru.foodcare.foodcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.foodcare.foodcare.ui.theme.FoodcareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodcareTheme {
                Content()
            }
        }
    }
}

@Composable
fun Content() {
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FoodcareTheme {
        Content()
    }
}