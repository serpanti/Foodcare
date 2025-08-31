package ru.foodcare.foodcare.presentation.composable

import androidx.compose.runtime.Composable
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import java.time.LocalDate

@Composable
fun MainWindow(
    mealViewModel: MealViewModel,
    openMealEditor: () -> Unit
) {
    DayMealWindowByDate(mealViewModel, LocalDate.now(), openMealEditor)
}