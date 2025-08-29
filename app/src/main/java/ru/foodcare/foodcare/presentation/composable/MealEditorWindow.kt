package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.presentation.input.InputMeal
import ru.foodcare.foodcare.presentation.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel
import java.time.LocalDateTime

@Composable
fun MealEditWindow(viewModel: MealViewModel, productViewModel: ProductViewModel,
                   close: () -> Unit) {
    val meal = remember {viewModel.mealObserved}

    CardSurface(
        Modifier
            .fillMaxSize()
            .padding(bottom = 15.dp)
            .padding(5.dp)
            .background(Color.Transparent)
            .padding(10.dp)
    ) {
        MealEditCard(meal, viewModel, productViewModel, close)
    }
}

@Composable
fun MealEditCard(oldMeal: Meal?, viewModel: MealViewModel,
                 productViewModel: ProductViewModel, close: () -> Unit) {
    val id = remember { oldMeal?.id ?: 0 }
    val product = remember { mutableStateOf<Product?>(oldMeal?.product) }
    val year = remember { mutableStateOf(oldMeal?.year?.toString() ?: "") }
    val month = remember { mutableStateOf(oldMeal?.month?.toString() ?: "") }
    val day = remember { mutableStateOf(oldMeal?.day?.toString() ?: "") }
    val hours = remember { mutableStateOf(oldMeal?.hours?.toString() ?: "") }
    val minutes = remember { mutableStateOf(oldMeal?.minutes?.toString() ?: "") }
    val seconds = remember { mutableStateOf(oldMeal?.seconds?.toString() ?: "") }

    LazyColumn {
        itemWithUnderLine {ProductEdit(product, productViewModel)}
        itemWithUnderLine {TimeEdit(year, month, day, hours, minutes, seconds)}
        item {
            val yearNum = year.value.parseToIntOrNull()
            val monthNum = month.value.parseToIntOrNull()
            val dayNum = day.value.parseToIntOrNull()
            val hoursNum = hours.value.parseToIntOrNull()
            val minutesNum = minutes.value.parseToIntOrNull()
            val secondsNum = seconds.value.parseToIntOrNull()
            val inputMeal = InputMeal(id, product.value,
                yearNum, monthNum, dayNum, hoursNum, minutesNum, secondsNum)

            SaveButton(oldMeal == null, inputMeal, close,
                {viewModel.addMeal(inputMeal.toMeal())},
                {viewModel.updateMeal(inputMeal.toMeal())}) {
                WrongInputMealPreview(inputMeal)
            }
        }
    }
}

@Composable
fun ProductEdit(product: MutableState<Product?>, productViewModel: ProductViewModel) {
    Box {
        SearchProductFieldWithList(productViewModel) { visible, close, products ->
            DropdownProducts(visible, close, products,
                Modifier.fillMaxWidth().height(100.dp),
                offset = DpOffset(0.dp, 0.dp)) { newProduct ->
                product.value = newProduct
            }
        }
    }
}

@Composable
fun TimeEdit(
    year: MutableState<String>,
    month: MutableState<String>,
    day: MutableState<String>,
    hours: MutableState<String>,
    minutes: MutableState<String>,
    seconds: MutableState<String>
) {
    Column {
        SquareButton("Установить Текущее время") {
            setSystemTime(year, month, day, hours, minutes, seconds)
        }
        YearTextField(year)
        MonthTextField(month)
        DayTextField(day)
        HoursTextField(hours)
        MinutesTextField(minutes)
        SecondsTextField(seconds)
    }
}

@Composable
fun YearTextField(year: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(year.value, onValueChange = { newStr ->
        year.value = newStr
    }, modifier = modifier, placeholder = {
        Text("Введите год полностью")
    }, prefix = {
        Text("Год: ")
    })
}

@Composable
fun MonthTextField(month: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(month.value, onValueChange = { newStr ->
        month.value = newStr
    }, modifier = modifier, placeholder = {
        Text("Введите номер месяца (1-12)")
    }, prefix = {
        Text("Месяц: ")
    })
}

@Composable
fun DayTextField(day: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(day.value, onValueChange = { newStr ->
        day.value = newStr
    }, modifier = modifier, placeholder = {
        Text("Введите номер дня (1-31)")
    }, prefix = {
        Text("День: ")
    })
}

@Composable
fun HoursTextField(hour: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(hour.value, onValueChange = { newStr ->
        hour.value = newStr
    }, modifier = modifier, placeholder = {
        Text("Введите номер часа (0-23)")
    }, prefix = {
        Text("Часы: ")
    })
}

@Composable
fun MinutesTextField(minutes: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(minutes.value, onValueChange = { newStr ->
        minutes.value = newStr
    }, modifier = modifier, placeholder = {
        Text("Введите количество минут (0-59)")
    }, prefix = {
        Text("Минуты: ")
    })
}

@Composable
fun SecondsTextField(seconds: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(seconds.value, onValueChange = { newStr ->
        seconds.value = newStr
    }, modifier = modifier, placeholder = {
        Text("Введите количество секунд (0-59)")
    }, prefix = {
        Text("Секунды: ")
    })
}

private fun setSystemTime(year: MutableState<String>,
                  month: MutableState<String>,
                  day: MutableState<String>,
                  hours: MutableState<String>,
                  minutes: MutableState<String>,
                  seconds: MutableState<String>) {
    val time = LocalDateTime.now()
    year.value = time.year.toString()
    month.value = time.monthValue.toString()
    day.value = time.dayOfMonth.toString()
    hours.value = time.hour.toString()
    minutes.value = time.minute.toString()
    seconds.value = time.second.toString()
}

@Composable
fun WrongInputMealPreview(inputMeal: InputMeal) {
    Column {
        if (inputMeal.product == null) Text("Продукт не выбран")
        if (inputMeal.year == null) Text("Ошибка в году")
        if (inputMeal.month == null) Text("Ошибка в месяце")
        if (inputMeal.day == null) Text("Ошибка в дне")
        if (inputMeal.hours == null) Text("Ошибка в часах")
        if (inputMeal.minutes == null) Text("Ошибка в минутах")
        if (inputMeal.seconds == null) Text("Ошибка в секундах")
        if (inputMeal.year != null && inputMeal.month != null && inputMeal.day != null &&
            inputMeal.hours != null && inputMeal.minutes != null && inputMeal.seconds != null
            && inputMeal.product != null) {
                try {
                    LocalDateTime.of(inputMeal.year, inputMeal.month, inputMeal.day,
                        inputMeal.hours, inputMeal.minutes, inputMeal.seconds)
                } catch (_: Exception) {
                    Text("Такой даты не существует")
                }
            }
    }
}
