package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CalendarWindow() {
    val formatType = remember { mutableStateOf<CalendarShowType>(CalendarShowType.Week) }
    Box(modifier = Modifier.fillMaxSize()) {
        CalendarTypeSelector(formatType, Modifier
            .align(Alignment.BottomEnd)
            .offset((-10).dp, (-10).dp))
        when (formatType.value) {
            CalendarShowType.Week -> {WeeksWindow()}
            CalendarShowType.Month -> {MonthsWindow()}
            CalendarShowType.Year -> {YearsWindow()}
        }
    }
}

@Composable
fun WeeksWindow() {

}

@Composable
fun MonthsWindow() {

}

@Composable
fun YearsWindow() {

}

@Composable
fun CalendarTypeSelector(type: MutableState<CalendarShowType>, modifier: Modifier = Modifier) {
    Row(modifier = modifier.height(IntrinsicSize.Max)
        .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .padding(2.dp)
    ) {
        CalendarTypeButton("7Д.", type.value == CalendarShowType.Week) {
            type.value = CalendarShowType.Week
        }
        VerticalDivider(2.dp, Color.Black)
        CalendarTypeButton("1М.", type.value == CalendarShowType.Month) {
            type.value = CalendarShowType.Month
        }
        VerticalDivider(2.dp, Color.Black)
        CalendarTypeButton("1Г.", type.value == CalendarShowType.Year) {
            type.value = CalendarShowType.Year
        }
    }
}

@Composable
fun CalendarTypeButton(text: String, selected: Boolean, onSelect: () -> Unit) {
    Box(modifier = Modifier
        .selectable(selected) {
            onSelect()
        }
        .background(if (selected) Color.DarkGray else Color.Gray)
        .padding(5.dp)
    ) {
        Text(text, color = if (selected) Color.White else Color.Black)
    }
}

sealed class CalendarShowType() {
    object Week: CalendarShowType()
    object Month: CalendarShowType()
    object Year: CalendarShowType()
}
