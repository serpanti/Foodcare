package ru.foodcare.foodcare.presentation.composable

import androidx.compose.runtime.Composable
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import java.time.LocalDate

@Composable
fun MainWindow(
    mealViewModel: MealViewModel,
    openMealEditor: () -> Unit
) {
    val today = LocalDate.now()
    val year = today.year
    val month = today.month.value
    val day = today.dayOfMonth

    DayMealWindowByDate(mealViewModel, year, month, day, openMealEditor)
}