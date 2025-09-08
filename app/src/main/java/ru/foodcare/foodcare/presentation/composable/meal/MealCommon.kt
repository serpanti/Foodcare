package ru.foodcare.foodcare.presentation.composable.meal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.presentation.composable.AlertDeleteDialog
import ru.foodcare.foodcare.presentation.composable.IconWithAction
import ru.foodcare.foodcare.presentation.composable.toStringWithLanguage
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel

@Composable
fun MealCard(meal: Meal, mealViewModel: MealViewModel,
             modifier: Modifier = Modifier, openMealEditor: () -> Unit) {
    var showAlert by remember { mutableStateOf(false) }
    if (showAlert) {
        AlertDeleteMealDialog({showAlert = false}, meal) {
            mealViewModel.removeMeal(meal)
        }
    }
    MealCardContent(meal, modifier.fillMaxSize()
        .padding(10.dp)
        .clickable {
            mealViewModel.onUpdateMeal(meal)
            openMealEditor()
        }) {
        showAlert = true
    }
}

@Composable
fun MealCardContent(meal: Meal, modifier: Modifier = Modifier,
                    delete: () -> Unit = {}) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        MealCardContentInfo(meal)
        Column (Modifier.wrapContentSize()) {
            IconWithAction(
                delete, Modifier
                    .size(50.dp).clip(CircleShape)
            )
            Text("%02d:%02d".format(meal.date.hour, meal.date.minute))
        }
    }
}

@Composable
fun MealCardContentInfoWithTime(meal: Meal, modifier: Modifier = Modifier,
                                other: @Composable () -> Unit = {}) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        MealCardContentInfo(meal)
        Column (Modifier.wrapContentSize().align(Alignment.Bottom)) {
            other()
            Text("%02d:%02d".format(meal.date.hour, meal.date.minute))
        }
    }
}

@Composable
fun MealCardContentInfo(meal: Meal, modifier: Modifier = Modifier) {
    val product = meal.product
    val productCount = "Кол-во: %d %s".format(
        (product.amount * meal.productRatio).toInt(), product.type.toStringWithLanguage())
    Column(modifier.wrapContentSize()) {
        Text("Имя: " + product.name)
        Text("Производитель: " + product.production)
        Text(productCount)
    }
}

@Composable
fun AlertDeleteMealDialog(close: () -> Unit, meal: Meal, delete: () -> Unit) =
    AlertDeleteDialog(close, delete) {
        MealCardContentInfoWithTime(meal)
    }
