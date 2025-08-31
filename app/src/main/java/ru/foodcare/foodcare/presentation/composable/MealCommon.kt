package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel

@Composable
fun DayMealWindowByDate(mealViewModel: MealViewModel,
                        year: Int, month: Int, day: Int,
                        openMealEditor: () -> Unit) {
    val observedDay = mealViewModel.observeDay(year, month, day).collectAsState()
    Box(Modifier.fillMaxSize()) {
        ElementWithHeader({
            Header("%02d/%02d/%d".format(day, month, year))
        }) {
            Meals(observedDay, mealViewModel, openMealEditor)
        }
        val offset = (-10).dp
        FloatingAddButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(offset.withLayoutDirection(), offset), openMealEditor)
    }
}