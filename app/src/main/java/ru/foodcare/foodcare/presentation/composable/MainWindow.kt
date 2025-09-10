package ru.foodcare.foodcare.presentation.composable

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.presentation.viewModel.weight.WeightViewModel
import java.time.LocalDate

@Composable
fun MainWindow(
    mealViewModel: MealViewModel,
    weightVM: WeightViewModel,
    snackbarHostState: SnackbarHostState? = null,
    openMealEditor: () -> Unit,
    openWeightEditor: () -> Unit
) {
    DayWindowByDate(
        mealViewModel,
        weightVM,
        snackbarHostState,
        LocalDate.now(),
        openWeightEditor,
        openMealEditor
    )
}