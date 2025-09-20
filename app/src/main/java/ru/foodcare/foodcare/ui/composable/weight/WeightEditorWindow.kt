package ru.foodcare.foodcare.ui.composable.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.domain.weight.Weight
import ru.foodcare.foodcare.ui.composable.CardSurface
import ru.foodcare.foodcare.ui.composable.SaveButton
import ru.foodcare.foodcare.ui.composable.SimpleTextField
import ru.foodcare.foodcare.ui.composable.TimeEdit
import ru.foodcare.foodcare.ui.composable.parseToDoubleOrNull
import ru.foodcare.foodcare.ui.composable.parseToIntOrNull
import ru.foodcare.foodcare.ui.input.InputWeight
import ru.foodcare.foodcare.ui.viewModel.weight.WeightViewModel
import java.time.LocalDateTime

@Composable
fun WeightEditWindow(viewModel: WeightViewModel,
                   close: () -> Unit) {
    val weight = viewModel.weightObserved.collectAsState()

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        CardSurface(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
                .padding(5.dp)
                .background(Color.Transparent)
                .padding(10.dp)
        ) {
            WeightEditCard(weight.value, viewModel, close)
        }
    }
}

@Composable
fun WeightEditCard(oldWeight: Weight?, viewModel: WeightViewModel, close: () -> Unit) {
    val id = rememberSaveable { oldWeight?.id ?: 0 }
    val weightValue = rememberSaveable { mutableStateOf(oldWeight?.value?.toString() ?: "") }
    val year = rememberSaveable { mutableStateOf(oldWeight?.date?.year?.toString() ?: "") }
    val month = rememberSaveable { mutableStateOf(oldWeight?.date?.monthValue?.toString() ?: "") }
    val day = rememberSaveable { mutableStateOf(oldWeight?.date?.dayOfMonth?.toString() ?: "") }
    val hours = rememberSaveable { mutableStateOf(oldWeight?.date?.hour?.toString() ?: "") }
    val minutes = rememberSaveable { mutableStateOf(oldWeight?.date?.minute?.toString() ?: "") }
    val seconds = rememberSaveable { mutableStateOf(oldWeight?.date?.second?.toString() ?: "") }

    Column {
        WeightEdit(weightValue,
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp))
        HorizontalDivider()
        TimeEdit(year, month, day, hours, minutes, seconds,
            Modifier.padding(bottom = 5.dp))
        HorizontalDivider()

        val weightValueNum = weightValue.value.parseToDoubleOrNull()
        val yearNum = year.value.parseToIntOrNull()
        val monthNum = month.value.parseToIntOrNull()
        val dayNum = day.value.parseToIntOrNull()
        val hoursNum = hours.value.parseToIntOrNull()
        val minutesNum = minutes.value.parseToIntOrNull()
        val secondsNum = seconds.value.parseToIntOrNull()
        val inputWeight = InputWeight(id, weightValueNum,
            yearNum, monthNum, dayNum, hoursNum, minutesNum, secondsNum)

        SaveButton(
            oldWeight == null, inputWeight, close,
            { viewModel.add(inputWeight.toWeight()) },
            { viewModel.update(inputWeight.toWeight()) }) {
            WrongInputWeightPreview(inputWeight)
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
        Text(stringResource(R.string.weight_edit_placeholder))
    }, prefix = {
        Text(stringResource(R.string.weight_capital) + ": ")
    }, suffix = {
        Text(" " + stringResource(R.string.kg))
    })
}

@Composable
fun WrongInputWeightPreview(inputWeight: InputWeight) {
    Column {
        if (inputWeight.value == null) Text(stringResource(R.string.weight_error_message))
        if (inputWeight.year == null) Text(stringResource(R.string.year_error_message))
        if (inputWeight.month == null) Text(stringResource(R.string.month_error_message))
        if (inputWeight.day == null) Text(stringResource(R.string.day_error_message))
        if (inputWeight.hours == null) Text(stringResource(R.string.hours_error_message))
        if (inputWeight.minutes == null) Text(stringResource(R.string.minutes_error_message))
        if (inputWeight.seconds == null) Text(stringResource(R.string.seconds_error_message))
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
                Text(stringResource(R.string.wrong_date_message))
            }
        }
    }
}
