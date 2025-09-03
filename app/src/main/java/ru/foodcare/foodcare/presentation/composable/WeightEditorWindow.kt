package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.domain.weight.Weight
import ru.foodcare.foodcare.presentation.input.InputWeight
import ru.foodcare.foodcare.presentation.viewModel.weight.WeightViewModel
import java.time.LocalDateTime

@Composable
fun WeightEditWindow(viewModel: WeightViewModel,
                   close: () -> Unit) {
    val weight = viewModel.weightObserved.collectAsState()

    CardSurface(
        Modifier
            .fillMaxSize()
            .padding(bottom = 15.dp)
            .padding(5.dp)
            .background(Color.Transparent)
            .padding(10.dp)
    ) {
        WeightEditCard(weight.value, viewModel, close)
    }
}

@Composable
fun WeightEditCard(oldWeight: Weight?, viewModel: WeightViewModel, close: () -> Unit) {
    val id = remember { oldWeight?.id ?: 0 }
    val weightValue = remember { mutableStateOf(oldWeight?.value?.toString() ?: "") }
    val year = remember { mutableStateOf(oldWeight?.date?.year?.toString() ?: "") }
    val month = remember { mutableStateOf(oldWeight?.date?.monthValue?.toString() ?: "") }
    val day = remember { mutableStateOf(oldWeight?.date?.dayOfMonth?.toString() ?: "") }
    val hours = remember { mutableStateOf(oldWeight?.date?.hour?.toString() ?: "") }
    val minutes = remember { mutableStateOf(oldWeight?.date?.minute?.toString() ?: "") }
    val seconds = remember { mutableStateOf(oldWeight?.date?.second?.toString() ?: "") }

    LazyColumn {
        itemWithUnderLine {WeightEdit(weightValue)}
        itemWithUnderLine {TimeEdit(year, month, day, hours, minutes, seconds)}
        item {
            val weightValueNum = weightValue.value.parseToDoubleOrNull()
            val yearNum = year.value.parseToIntOrNull()
            val monthNum = month.value.parseToIntOrNull()
            val dayNum = day.value.parseToIntOrNull()
            val hoursNum = hours.value.parseToIntOrNull()
            val minutesNum = minutes.value.parseToIntOrNull()
            val secondsNum = seconds.value.parseToIntOrNull()
            val inputWeight = InputWeight(id, weightValueNum,
                yearNum, monthNum, dayNum, hoursNum, minutesNum, secondsNum)

            SaveButton(oldWeight == null, inputWeight, close,
                {viewModel.add(inputWeight.toWeight())},
                {viewModel.update(inputWeight.toWeight())}) {
                WrongInputWeightPreview(inputWeight)
            }
        }
    }
}

@Composable
fun WeightEdit(
    weightValue: MutableState<String>,
    modifier: Modifier = Modifier
) {
    SimpleTextField(weightValue.value, onValueChange = { newStr ->
        weightValue.value = newStr
    }, modifier = modifier, placeholder = {
        Text("Введите ваш вес в килограммах(50.73)")
    }, prefix = {
        Text("Вес: ")
    }, suffix = {
        Text(" кг")
    })
}

@Composable
fun WrongInputWeightPreview(inputWeight: InputWeight) {
    Column {
        if (inputWeight.value == null) Text("Ошибка в весе")
        if (inputWeight.year == null) Text("Ошибка в году")
        if (inputWeight.month == null) Text("Ошибка в месяце")
        if (inputWeight.day == null) Text("Ошибка в дне")
        if (inputWeight.hours == null) Text("Ошибка в часах")
        if (inputWeight.minutes == null) Text("Ошибка в минутах")
        if (inputWeight.seconds == null) Text("Ошибка в секундах")
        if (inputWeight.year != null &&
            inputWeight.month != null &&
            inputWeight.day != null &&
            inputWeight.hours != null &&
            inputWeight.minutes != null &&
            inputWeight.seconds != null) {
            try {
                LocalDateTime.of(inputWeight.year, inputWeight.month, inputWeight.day,
                    inputWeight.hours, inputWeight.minutes, inputWeight.seconds)
            } catch (_: Exception) {
                Text("Такой даты не существует")
            }
        }
    }
}
