package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel

@Composable
fun CalendarWindow(mealViewModel: MealViewModel) {
    val formatType = remember { mutableStateOf<CalendarShowType>(CalendarShowType.Years) }
    val offset = (-10).dp

    Box(modifier = Modifier.fillMaxSize()) {
        CalendarTypeSelector(formatType, Modifier
            .align(Alignment.BottomEnd)
            .offset(offset.withLayoutDirection(), offset))
        when (formatType.value) {
            CalendarShowType.Day -> {DayWindow(formatType, mealViewModel)}
            CalendarShowType.Days -> {DaysWindow(formatType, mealViewModel)}
            CalendarShowType.Months -> {MonthsWindow(formatType, mealViewModel)}
            CalendarShowType.Years -> {YearsWindow(formatType, mealViewModel)}
        }
    }
}

@Composable
fun DayWindow(formatType: MutableState<CalendarShowType>, mealViewModel: MealViewModel) {
    if (mealViewModel.observedMonth.value == null ||
        mealViewModel.observedYear.value == null ||
        mealViewModel.observedDay.value == null) {
        AdviceButton("Сначала выберите день") {
            formatType.value = CalendarShowType.Days
        }
    }
    mealViewModel.observedYear.value ?. let { year ->
        mealViewModel.observedMonth.value ?. let { month ->
            mealViewModel.observedDay.value ?. let { day ->
                val observedDay = mealViewModel.observeDay(year, month, day).collectAsState()
                Meals(observedDay)
            }
        }
    }
}

@Composable
fun DaysWindow(formatType: MutableState<CalendarShowType>, mealViewModel: MealViewModel) {
    if (mealViewModel.observedMonth.value == null || mealViewModel.observedYear.value == null) {
        AdviceButton("Сначала выберите месяц") {
            formatType.value = CalendarShowType.Months
        }
    }
    mealViewModel.observedYear.value ?. let { year ->
        mealViewModel.observedMonth.value ?. let { month ->
            val days = mealViewModel.observeDays(year, month).collectAsState()

            LazyVerticalGrid(GridCells.Fixed(5),
                contentPadding = PaddingValues(bottom = 50.dp)) {
                items(days.value.size) { idx ->
                    SquareButton(days.value[idx].toString()) {
                        mealViewModel.observedDay.value = days.value[idx]
                        formatType.value = CalendarShowType.Day
                    }
                }
            }
        }
    }
}

private fun Int.toMonth(): String {
    val year = listOf(
        "январь", "февраль", "март", "апрель",
        "май", "июнь", "июль", "август",
        "сентябрь", "октябрь", "ноябрь", "декабрь"
    )

    return year[(this - 1) % year.size]
}

@Composable
fun MonthsWindow(formatType: MutableState<CalendarShowType>, mealViewModel: MealViewModel) {
    if (mealViewModel.observedYear.value == null) {
        AdviceButton("Сначала выберите год") {
            formatType.value = CalendarShowType.Years
        }
    }
    mealViewModel.observedYear.value ?. let {
        val months = mealViewModel.observeMonths(it).collectAsState()

        LazyVerticalGrid(GridCells.Fixed(3),
            contentPadding = PaddingValues(bottom = 50.dp)) {
            items(months.value.size) { idx ->
                SquareButton(months.value[idx].toMonth()) {
                    mealViewModel.observedMonth.value = months.value[idx]
                    formatType.value = CalendarShowType.Days
                }
            }
        }
    }
}

@Composable
fun YearsWindow(formatType: MutableState<CalendarShowType>, mealViewModel: MealViewModel) {
    val years = mealViewModel.observedYears.collectAsState()

    ElementWithHeader({
        Header("Все года")
    }) {
        if (years.value.isNotEmpty()) {
            YearsTable(years, formatType, mealViewModel)
        } else {
            AdviceButton("Пока еще не было сделано ни одной записи") {
                // TODO отправить делать запись
            }
        }
    }
}

@Composable
fun YearsTable(
    years: State<List<Int>>,
    formatType: MutableState<CalendarShowType>,
    mealViewModel: MealViewModel
) {
    LazyVerticalGrid(GridCells.Fixed(5),
        contentPadding = PaddingValues(bottom = 50.dp)) {
        items(years.value.size) { idx ->
            SquareButton(years.value[idx].toString()) {
                mealViewModel.observedYear.value = years.value[idx]
                formatType.value = CalendarShowType.Months
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

@Composable
fun Meals(meals: State<List<Meal>>) {
    LazyColumn {
        items(meals.value.size) { idx ->
            MealCard(meals.value[idx])
        }
    }
}

@Composable
fun MealCard(meal: Meal, modifier: Modifier = Modifier) {
    val product = meal.product
    CardSurface(modifier.fillMaxWidth().padding(10.dp)) {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            Column {
                Text("Имя: " + product.name)
                Text("Производитель: " + product.production)
            }
            Text("%02d:%02d".format(meal.hours, meal.minutes),
                modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
