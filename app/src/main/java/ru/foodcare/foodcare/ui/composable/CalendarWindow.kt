package ru.foodcare.foodcare.ui.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.nutrientsProperties.NutrientsProperties
import ru.foodcare.foodcare.domain.weight.Weight
import ru.foodcare.foodcare.ui.composable.meal.MealCard
import ru.foodcare.foodcare.ui.composable.nutrients.NutrientsPropertiesCard
import ru.foodcare.foodcare.ui.composable.weight.WeightCard
import ru.foodcare.foodcare.ui.model.MealItem
import ru.foodcare.foodcare.ui.model.WeightItem
import ru.foodcare.foodcare.ui.viewModel.date.DateViewModel
import ru.foodcare.foodcare.ui.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.ui.viewModel.weight.WeightViewModel
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

@Composable
fun CalendarWindow(
    mealViewModel: MealViewModel,
    dateVM: DateViewModel,
    weightVM: WeightViewModel,
    snackbarHostState: SnackbarHostState? = null,
    openWeightEditor: () -> Unit,
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

        AnimatedContent(targetState = formatType.value,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "CalendarContent"
            ) { state ->
            when (state) {
                CalendarShowType.Day -> {DayWindow(dateVM, mealViewModel, weightVM, snackbarHostState,
                    openWeightEditor, openMealEditor)}
                CalendarShowType.Days -> {DaysWindow(formatType, dateVM)}
                CalendarShowType.Months -> {MonthsWindow(formatType, dateVM)}
                CalendarShowType.Years -> {YearsWindow(formatType, dateVM)}
            }
        }
    }
}

@Composable
fun DayWindow(
    dateVM: DateViewModel,
    mealViewModel: MealViewModel,
    weightVM: WeightViewModel,
    snackbarHostState: SnackbarHostState? = null,
    openWeightEditor: () -> Unit,
    openMealEditor: () -> Unit
) {
    val date by dateVM.observedDate.collectAsState()

    InfiniteDatePager(
        setNewDate = { newDate -> dateVM.onObservedDateChanged(newDate) },
        currentDate = date,
        getChangedDate = {days -> this.plusDays(days)},
    ) { pageDate ->
        DayWindowByDate(mealViewModel, weightVM, snackbarHostState,
            pageDate, openWeightEditor, openMealEditor)
    }
}

@Composable
fun DayWindowByDate(
    mealViewModel: MealViewModel,
    weightVM: WeightViewModel,
    snackbarHostState: SnackbarHostState? = null,
    date: LocalDate,
    openWeightEditor: () -> Unit,
    openMealEditor: () -> Unit
) {
    val observedMeals = mealViewModel.observeDay(date).collectAsState()
    val observedWeights = weightVM.observeDay(date).collectAsState()
    Box(Modifier.fillMaxSize()) {
        ElementWithHeader({
            Header("%02d/%02d/%d".format(date.dayOfMonth, date.monthValue, date.year))
        }) {
            DayCards(observedMeals, observedWeights, mealViewModel, weightVM, snackbarHostState,
                openWeightEditor, openMealEditor)
        }
        val offset = (-10).dp
        FloatingAddListButton(modifier = Modifier.align(Alignment.BottomEnd),
            offset = DpOffset(offset.withLayoutDirection(), offset),
            listOf<@Composable (Modifier) -> Unit> (
                { modifier ->
                    SmallFloatingActionButton(onClick = {
                        mealViewModel.onAddMeal()
                        openMealEditor()
                    }, modifier = modifier) {
                        Icon(Icons.Filled.RestaurantMenu, stringResource(R.string.open_meal_editor))
                    }
                }, { modifier ->
                    SmallFloatingActionButton(onClick = {
                        weightVM.onAddWeight()
                        openWeightEditor()
                    }, modifier = modifier) {
                        Icon(Icons.Filled.Balance, stringResource(R.string.open_weight_editor))
                    }
                }
            )
        )
    }
}

@Composable
fun DayCards(
    meals: State<List<Meal>>,
    weights: State<List<Weight>>,
    mealViewModel: MealViewModel,
    weightVM: WeightViewModel,
    snackbarHostState: SnackbarHostState? = null,
    openWeightEditor: () -> Unit,
    openMealEditor: () -> Unit
) {
    val listState = rememberLazyListState()
    val dayList by remember {
        derivedStateOf {
            (meals.value.map{MealItem(it)} + weights.value.map{WeightItem(it)})
                .sortedByDescending  { it.date }
        }
    }

    LazyColumn (state = listState,
        contentPadding = PaddingValues(bottom = 600.dp)) {
        item {
            DayContentCard {
                NutrientsPropertiesCard(summarizeMeals(meals.value), Modifier.padding(10.dp))
            }
        }
        items(dayList) { cardContent ->
            DayContentCard {
                when (cardContent) {
                    is MealItem -> MealCard(cardContent.meal,
                            mealViewModel, snackbarHostState = snackbarHostState,
                        openMealEditor = openMealEditor)
                    is WeightItem -> WeightCard(cardContent.weight,
                            weightVM, snackbarHostState = snackbarHostState,
                        openWeightEditor = openWeightEditor)
                }
            }
        }
    }
}

private fun summarizeMeals(meals: List<Meal>): NutrientsProperties {
    val calories = meals.sumOf { it.product.nutrientsProperties.calories * it.productRatio }
    val protein = meals.sumOf { it.product.nutrientsProperties.protein * it.productRatio }
    val fat = meals.sumOf { it.product.nutrientsProperties.fat * it.productRatio }
    val carbohydrates = meals.sumOf {
        it.product.nutrientsProperties.carbohydrates * it.productRatio
    }
    val fiber = meals.sumOf { it.product.nutrientsProperties.fiber * it.productRatio }

    return NutrientsProperties(calories = calories,
        protein = protein, fat = fat, carbohydrates = carbohydrates,
        fiber = fiber)
}

@Composable
fun DayContentCard(modifier: Modifier = Modifier,
                   content: @Composable () -> Unit) {
    CardSurface(modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        content()
    }
}

@Composable
fun DaysWindow(formatType: MutableState<CalendarShowType>, dateVM: DateViewModel) {
    val date by dateVM.observedDate.collectAsState()

    val days = (1 .. date.lengthOfMonth()).toList()

    InfiniteDatePager(
        setNewDate = { newDate -> dateVM.onObservedDateChanged(newDate) },
        currentDate = date,
        getChangedDate = {months -> this.plusMonths(months)},
    ) { pageDate ->
        ElementWithHeader({
            Header("%s  %d %s."
                .format(date.monthValue.toMonth(), date.year,
                    stringResource(R.string.year_short)))
        }) {
            DaysTable(days, formatType, dateVM)
        }
    }
}

@Composable
fun DaysTable(
    days: List<Int>,
    formatType: MutableState<CalendarShowType>,
    dateVM: DateViewModel
) {
    val date by dateVM.observedDate.collectAsState()
    val now = LocalDate.now()

    LazyVerticalGrid(GridCells.Fixed(5),
        contentPadding = PaddingValues(bottom = 50.dp)) {
        items(days.size) { idx ->
            val colors = blinkColorButtonAsState(days[idx] == date.dayOfMonth,
                date.year == now.year && date.month == now.month && days[idx] == now.dayOfMonth)

            SquareButton(days[idx].toString(),
                backgroundColor = colors.first.value,
                color = colors.second.value) {
                val newDate = if (days[idx] in (1 .. date.lengthOfMonth())) {
                    date.withDayOfMonth(days[idx])
                } else {
                    date
                }

                dateVM.onObservedDateChanged(newDate)
                formatType.value = CalendarShowType.Day
            }
        }
    }
}

@Composable
private fun Int.toMonth(): String {
    val year = stringArrayResource(R.array.months)

    return year[(this - 1) % year.size]
}

@Composable
fun MonthsWindow(formatType: MutableState<CalendarShowType>, dateVM: DateViewModel) {
    val months = remember {(1..12).toList()}
    val date by dateVM.observedDate.collectAsState()

    InfiniteDatePager(
        setNewDate = { newDate -> dateVM.onObservedDateChanged(newDate) },
        currentDate = date,
        getChangedDate = {years -> this.plusYears(years)},
    ) { pageDate ->
        ElementWithHeader({
            Header("%d %s".format(date.year, stringResource(R.string.year)))
        }) {
            MonthsTable(months, formatType, dateVM)
        }
    }
}

@Composable
fun MonthsTable(
    months: List<Int>,
    formatType: MutableState<CalendarShowType>,
    dateVM: DateViewModel
) {
    val date by dateVM.observedDate.collectAsState()
    val now = LocalDate.now()

    LazyVerticalGrid(GridCells.Fixed(3),
        contentPadding = PaddingValues(bottom = 50.dp)) {
        items(months.size) { idx ->
            val colors = blinkColorButtonAsState(months[idx] == date.monthValue,
                date.year == now.year && months[idx] == now.monthValue)

            SquareButton(months[idx].toMonth(),
                backgroundColor = colors.first.value,
                color = colors.second.value) {
                if (months[idx] in (1 .. date.lengthOfMonth())) {
                    val newMonthLength = YearMonth.of(date.year, months[idx]).lengthOfMonth()
                    val newDay = minOf(date.dayOfMonth, newMonthLength)
                    val newDate = LocalDate.of(date.year, months[idx], newDay)

                    dateVM.onObservedDateChanged(newDate)
                    formatType.value = CalendarShowType.Days
                }
            }
        }
    }
}

@Composable
fun YearsWindow(formatType: MutableState<CalendarShowType>, dateVM: DateViewModel) {
    val years by dateVM.observedYears.collectAsState()
    val currentYear = LocalDate.now().year

    ElementWithHeader({
        Header(stringResource(R.string.all_years))
    }) {
        YearsTable((years + currentYear).distinct(), formatType, dateVM)
    }
}

@Composable
fun YearsTable(
    years: List<Int>,
    formatType: MutableState<CalendarShowType>,
    dateVM: DateViewModel
) {
    val date by dateVM.observedDate.collectAsState()
    val now = LocalDate.now()

    LazyVerticalGrid(GridCells.Fixed(5),
    contentPadding = PaddingValues(bottom = 50.dp)) {
        items(years.size) { idx ->
            val colors = blinkColorButtonAsState(years[idx] == date.year,
                years[idx] == now.year)

            SquareButton(years[idx].toString(),
                backgroundColor = colors.first.value,
                color = colors.second.value) {
                if (years[idx] in (Year.MIN_VALUE .. Year.MAX_VALUE)) {
                    val newMonthLength = YearMonth.of(years[idx], date.month).lengthOfMonth()
                    val newDay = minOf(date.dayOfMonth, newMonthLength)
                    val newDate = LocalDate.of(years[idx], date.month, newDay)

                    dateVM.onObservedDateChanged(newDate)
                    formatType.value = CalendarShowType.Months
                }
            }
        }
    }
}

@Composable
fun CalendarTypeSelector(type: MutableState<CalendarShowType>, modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .height(IntrinsicSize.Max)
        .width(160.dp)
        .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .padding(2.dp)
        .zIndex(1f)
    ) {
        CalendarTypeButton(stringResource(R.string.year_short_capital) + ".",
            type.value == CalendarShowType.Years, Modifier.weight(1f)) {
            type.value = CalendarShowType.Years
        }
        VerticalDivider()
        CalendarTypeButton(stringResource(R.string.month_short_capital) + ".",
            type.value == CalendarShowType.Months, Modifier.weight(1f)) {
            type.value = CalendarShowType.Months
        }
        VerticalDivider()
        CalendarTypeButton(stringResource(R.string.day_short_capital) + ".",
            type.value == CalendarShowType.Days, Modifier.weight(1f)) {
            type.value = CalendarShowType.Days
        }
        VerticalDivider()
        CalendarTypeButton("1" + stringResource(R.string.day_short_capital) + ".",
            type.value == CalendarShowType.Day, Modifier.weight(1f)) {
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
