package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import java.time.LocalDate

@Composable
fun DayMealWindowByDate(mealViewModel: MealViewModel,
                        date: LocalDate,
                        openMealEditor: () -> Unit) {
    val observedDay = mealViewModel.observeDay(date).collectAsState()
    Box(Modifier.fillMaxSize()) {
        ElementWithHeader({
            Header("%02d/%02d/%d".format(date.dayOfMonth, date.monthValue, date.year))
        }) {
            Meals(observedDay, mealViewModel, openMealEditor)
        }
        val offset = (-10).dp
        FloatingAddButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(offset.withLayoutDirection(), offset),
            openEditor = {
                mealViewModel.onAddMeal()
                openMealEditor()
            }
        )
    }
}

@Composable
fun Meals(meals: State<List<Meal>>, mealViewModel: MealViewModel, openMealEditor: () -> Unit) {
    val listState = rememberLazyListState()
    LaunchedEffect(meals.value.size) {
        if (meals.value.isNotEmpty()) {
            listState.animateScrollToItem(meals.value.lastIndex)
        }
    }

    LazyColumn (state = listState,
        contentPadding = PaddingValues(bottom = 600.dp)) {
        items(meals.value.size) { idx ->
            MealCard(meals.value[idx], mealViewModel, openMealEditor = openMealEditor)
        }
    }
}

@Composable
fun MealCard(meal: Meal, mealViewModel: MealViewModel,
             modifier: Modifier = Modifier, openMealEditor: () -> Unit) {
    CardSurface(modifier.fillMaxWidth().padding(10.dp)) {
        var showAlert by remember { mutableStateOf(false) }
        if (showAlert) {
            AlertDeleteMealDialog({showAlert = false}, meal) {
                mealViewModel.removeMeal(meal)
            }
        }
        MealCardContent(meal, Modifier.fillMaxSize()
            .padding(10.dp)
            .clickable {
                mealViewModel.onUpdateMeal(meal)
                openMealEditor()
            }) {
            showAlert = true
        }
    }
}

@Composable
fun MealCardContent(meal: Meal, modifier: Modifier = Modifier,
                    delete: () -> Unit = {}) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        MealCardContentInfo(meal)
        Column (Modifier.wrapContentSize()) {
            IconWithAction(delete, Modifier
                .size(50.dp).clip(CircleShape))
            Text("%02d:%02d".format(meal.hours, meal.minutes))
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
            Text("%02d:%02d".format(meal.hours, meal.minutes))
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
