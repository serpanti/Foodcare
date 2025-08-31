package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

@Composable
fun CalendarWindow(
    mealViewModel: MealViewModel,
    openMealEditor: () -> Unit
) {
    val formatType = rememberSaveable(stateSaver = CalendarShowTypeSaver) {
        mutableStateOf<CalendarShowType>(CalendarShowType.Years)
    }
    val yOffset = (-15).dp
    val xOffset = 15.dp

    Box(modifier = Modifier.fillMaxSize()) {
        CalendarTypeSelector(formatType, Modifier
            .align(Alignment.BottomStart)
            .offset(xOffset.withLayoutDirection(), yOffset))
        when (formatType.value) {
            CalendarShowType.Day -> {DayWindow(mealViewModel, openMealEditor)}
            CalendarShowType.Days -> {DaysWindow(formatType, mealViewModel)}
            CalendarShowType.Months -> {MonthsWindow(formatType, mealViewModel)}
            CalendarShowType.Years -> {YearsWindow(formatType, mealViewModel)}
        }
    }
}

@Composable
fun DayWindow(
    mealViewModel: MealViewModel,
    openMealEditor: () -> Unit
) {
    val date by mealViewModel.observedDate.collectAsState()

    DayMealWindowByDate(mealViewModel, date, openMealEditor)
}

@Composable
fun DaysWindow(formatType: MutableState<CalendarShowType>, mealViewModel: MealViewModel) {
    val date by mealViewModel.observedDate.collectAsState()

    val days = (1 .. date.lengthOfMonth()).toList()

    ElementWithHeader({
        Header("%s  %d г.".format(date.monthValue.toMonth(), date.year))
    }) {
        DaysTable(days, formatType, mealViewModel)
    }
}

@Composable
fun DaysTable(
    days: List<Int>,
    formatType: MutableState<CalendarShowType>,
    mealViewModel: MealViewModel
) {
    val date by mealViewModel.observedDate.collectAsState()

    LazyVerticalGrid(GridCells.Fixed(5),
        contentPadding = PaddingValues(bottom = 50.dp)) {
        items(days.size) { idx ->
            SquareButton(days[idx].toString()) {
                val newDate = if (days[idx] in (1 .. date.lengthOfMonth())) {
                    date.withDayOfMonth(days[idx])
                } else {
                    date
                }

                mealViewModel.onObservedDateChanged(newDate)
                formatType.value = CalendarShowType.Day
            }
        }
    }
}

@Composable
private fun Int.toMonth(): String {
    // TODO resources
    val year = listOf(
        "январь", "февраль", "март", "апрель",
        "май", "июнь", "июль", "август",
        "сентябрь", "октябрь", "ноябрь", "декабрь"
    )

    return year[(this - 1) % year.size]
}

@Composable
fun MonthsWindow(formatType: MutableState<CalendarShowType>, mealViewModel: MealViewModel) {
    val months = remember {(1..12).toList()}
    val date by mealViewModel.observedDate.collectAsState()

    ElementWithHeader({
        Header("%d год".format(date.year))
    }) {
        MonthsTable(months, formatType, mealViewModel)
    }
}

@Composable
fun MonthsTable(
    months: List<Int>,
    formatType: MutableState<CalendarShowType>,
    mealViewModel: MealViewModel
) {
    val date by mealViewModel.observedDate.collectAsState()

    LazyVerticalGrid(GridCells.Fixed(3),
        contentPadding = PaddingValues(bottom = 50.dp)) {
        items(months.size) { idx ->
            SquareButton(months[idx].toMonth()) {
                if (months[idx] in (1 .. date.lengthOfMonth())) {
                    val newMonthLength = YearMonth.of(date.year, months[idx]).lengthOfMonth()
                    val newDay = minOf(date.dayOfMonth, newMonthLength)
                    val newDate = LocalDate.of(date.year, months[idx], newDay)

                    mealViewModel.onObservedDateChanged(newDate)
                    formatType.value = CalendarShowType.Days
                }
            }
        }
    }
}

@Composable
fun YearsWindow(formatType: MutableState<CalendarShowType>, mealViewModel: MealViewModel) {
    val years by mealViewModel.observedYears.collectAsState()
    val currentYear = LocalDate.now().year

    ElementWithHeader({
        Header("Все года")
    }) {
        YearsTable((years + currentYear).distinct(), formatType, mealViewModel)
    }
}

@Composable
fun YearsTable(
    years: List<Int>,
    formatType: MutableState<CalendarShowType>,
    mealViewModel: MealViewModel
) {
    val date by mealViewModel.observedDate.collectAsState()

    LazyVerticalGrid(GridCells.Fixed(5),
        contentPadding = PaddingValues(bottom = 50.dp)) {
        items(years.size) { idx ->
            SquareButton(years[idx].toString()) {
                if (years[idx] in (Year.MIN_VALUE .. Year.MAX_VALUE)) {
                    val newMonthLength = YearMonth.of(years[idx], date.month).lengthOfMonth()
                    val newDay = minOf(date.dayOfMonth, newMonthLength)
                    val newDate = LocalDate.of(years[idx], date.month, newDay)

                    mealViewModel.onObservedDateChanged(newDate)
                    formatType.value = CalendarShowType.Months
                }
            }
        }
    }
}

@Composable
fun CalendarTypeSelector(type: MutableState<CalendarShowType>, modifier: Modifier = Modifier) {
    Row(modifier = modifier.height(IntrinsicSize.Max).width(160.dp)
        .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .padding(2.dp)
        .zIndex(1f)
    ) {
        CalendarTypeButton("Г.", type.value == CalendarShowType.Years, Modifier.weight(1f)) {
            type.value = CalendarShowType.Years
        }
        VerticalDivider(2.dp, Color.Black)
        CalendarTypeButton("М.", type.value == CalendarShowType.Months, Modifier.weight(1f)) {
            type.value = CalendarShowType.Months
        }
        VerticalDivider(2.dp, Color.Black)
        CalendarTypeButton("Д.", type.value == CalendarShowType.Days, Modifier.weight(1f)) {
            type.value = CalendarShowType.Days
        }
        VerticalDivider(2.dp, Color.Black)
        CalendarTypeButton("1Д.", type.value == CalendarShowType.Day, Modifier.weight(1f)) {
            type.value = CalendarShowType.Day
        }
    }
}

@Composable
fun CalendarTypeButton(text: String, selected: Boolean, modifier: Modifier = Modifier,
                       onSelect: () -> Unit) {
    Box(modifier = modifier
        .selectable(selected) {
            onSelect()
        }
        .background(if (selected) Color.DarkGray else Color.Gray)
        .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (selected) Color.White else Color.Black)
    }
}

sealed class CalendarShowType() {
    object Day: CalendarShowType()
    object Days: CalendarShowType()
    object Months: CalendarShowType()
    object Years: CalendarShowType()
}

val CalendarShowTypeSaver = Saver<CalendarShowType, String>(
    save = { type ->
        when (type) {
            CalendarShowType.Day -> "Day"
            CalendarShowType.Days -> "Days"
            CalendarShowType.Months -> "Months"
            CalendarShowType.Years -> "Years"
        }
    },
    restore = { name ->
        when (name) {
            "Day" -> CalendarShowType.Day
            "Days" -> CalendarShowType.Days
            "Months" -> CalendarShowType.Months
            "Years" -> CalendarShowType.Years
            else -> CalendarShowType.Years
        }
    }
)
