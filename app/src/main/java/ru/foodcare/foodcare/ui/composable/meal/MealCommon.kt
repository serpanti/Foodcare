package ru.foodcare.foodcare.ui.composable.meal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.ui.composable.AlertDeleteDialog
import ru.foodcare.foodcare.ui.composable.IconWithAction
import ru.foodcare.foodcare.ui.composable.toStringWithLanguage
import ru.foodcare.foodcare.ui.viewModel.meal.MealViewModel

@Composable
fun MealCard(meal: Meal, mealViewModel: MealViewModel,
             modifier: Modifier = Modifier,
             snackbarHostState: SnackbarHostState? = null, openMealEditor: () -> Unit) {
    var showAlert by remember { mutableStateOf(false) }
    val deleteScope = rememberCoroutineScope()
    val removeMealAction = {mealViewModel.removeMeal(meal)}

    val deleteAction: () -> Unit = {
        if (snackbarHostState != null) {
            deleteScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Скоро произойдет удаление",
                    actionLabel = "Отмена",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.Dismissed) removeMealAction()
            }
        } else {
            removeMealAction()
        }
    }

    if (showAlert)  {
        AlertDeleteMealDialog({showAlert = false}, meal, delete = deleteAction)
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
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        MealCardContentInfo(meal)
        Column (Modifier
        .wrapContentHeight(Alignment.Bottom)
        .align(Alignment.Bottom)) {
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
