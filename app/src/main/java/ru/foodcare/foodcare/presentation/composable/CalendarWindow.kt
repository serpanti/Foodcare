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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
            CalendarShowType.Year -> {YearsWindow(formatType)}
        }
    }
}

@Composable
fun WeeksWindow() {

}

private fun sortMonths(months: List<String>): List<String> {
    val order = listOf(
        "январь", "февраль", "март", "апрель",
        "май", "июнь", "июль", "август",
        "сентябрь", "октябрь", "ноябрь", "декабрь"
    )

    return months.sortedBy { order.indexOf(it.lowercase()) }
}

@Composable
fun MonthsWindow() {
    val months = sortMonths(listOf("июнь", "июль", "август", "июнь", "июль", "август")) // TODO: настроить на реальный список месяцев

    LazyVerticalGrid(GridCells.Fixed(5),
        contentPadding = PaddingValues(bottom = 50.dp)) {
        items(months.size) { idx ->
            SquareButton(months[idx]) { }
        }
    }
}

@Composable
fun YearsWindow(formatType: MutableState<CalendarShowType>) {
    val years = (2024..2040).toList().sorted()  // TODO: настроить на реальный список годов

    LazyVerticalGrid(GridCells.Fixed(5),
        contentPadding = PaddingValues(bottom = 50.dp)) {
        items(years.size) { idx ->
            SquareButton(years[idx].toString()) { formatType.value = CalendarShowType.Month }
        }
    }
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
