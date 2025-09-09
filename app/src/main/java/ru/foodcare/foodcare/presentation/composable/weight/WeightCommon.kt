package ru.foodcare.foodcare.presentation.composable.weight

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.domain.weight.Weight
import ru.foodcare.foodcare.presentation.composable.AlertDeleteDialog
import ru.foodcare.foodcare.presentation.composable.IconWithAction
import ru.foodcare.foodcare.presentation.viewModel.weight.WeightViewModel

@Composable
fun WeightCardContent(weight: Weight, modifier: Modifier = Modifier,
                    delete: () -> Unit = {}) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        WeightCardContentInfo(weight)
        Column (Modifier.wrapContentSize()) {
            IconWithAction(
                delete, Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text("%02d:%02d".format(weight.date.hour, weight.date.minute))
        }
    }
}

@Composable
fun WeightCard(weight: Weight, weightVM: WeightViewModel,
               modifier: Modifier = Modifier, openWeightEditor: () -> Unit) {
    var showAlert by remember { mutableStateOf(false) }
    if (showAlert) {
        AlertDeleteWeightDialog({showAlert = false}, weight) {
            weightVM.remove(weight)
        }
    }
    WeightCardContent(weight, modifier
        .fillMaxSize()
        .padding(10.dp)
        .clickable {
            weightVM.onUpdateWeight(weight)
            openWeightEditor()
        }) {
        showAlert = true
    }
}

@Composable
fun AlertDeleteWeightDialog(close: () -> Unit, weight: Weight, delete: () -> Unit) =
    AlertDeleteDialog(close, delete) {
        WeightCardContentInfoWithTime(weight)
    }

@Composable
fun WeightCardContentInfoWithTime(weight: Weight, modifier: Modifier = Modifier,
                                other: @Composable () -> Unit = {}) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        WeightCardContentInfo(weight)
        Column (Modifier
            .wrapContentSize()
            .align(Alignment.Bottom)) {
            other()
            Text("%02d:%02d".format(weight.date.hour, weight.date.minute))
        }
    }
}

@Composable
fun WeightCardContentInfo(weight: Weight, modifier: Modifier = Modifier) {
    // TODO resources
    Text("Вес: ${ weight.value } кг",
        modifier = modifier)
}
