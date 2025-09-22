package ru.foodcare.foodcare.ui.composable.weight

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.domain.weight.Weight
import ru.foodcare.foodcare.ui.composable.AlertDeleteDialog
import ru.foodcare.foodcare.ui.composable.CardContent
import ru.foodcare.foodcare.ui.viewModel.weight.WeightViewModel

@Composable
fun WeightCardContent(weight: Weight, modifier: Modifier = Modifier,
                    delete: () -> Unit = {}) {
    CardContent(modifier = modifier, time = weight.date.toLocalTime(), delete = delete) { modifier ->
        WeightCardContentInfo(weight, modifier)
    }
}

@Composable
fun WeightCard(weight: Weight, weightVM: WeightViewModel,
               modifier: Modifier = Modifier,
               snackbarHostState: SnackbarHostState? = null, openWeightEditor: () -> Unit) {
    var showAlert by remember { mutableStateOf(false) }
    val deleteScope = rememberCoroutineScope()
    val removeWeightAction = {weightVM.remove(weight)}

    val actionLabel = stringResource(R.string.cancel)
    val message = stringResource(R.string.deletion_warning)

    val deleteAction: () -> Unit = {
        if (snackbarHostState != null) {
            deleteScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.Dismissed) removeWeightAction()
            }
        } else {
            removeWeightAction()
        }
    }

    if (showAlert) {
        AlertDeleteWeightDialog({showAlert = false}, weight, delete = deleteAction)
    }
    WeightCardContent(weight, modifier
        .fillMaxSize()
        .clickable {
            weightVM.onUpdateWeight(weight)
            openWeightEditor()
        }
        .padding(10.dp)) {
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
    Row(modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        WeightCardContentInfo(weight)
        Column (Modifier
            .wrapContentHeight(Alignment.Bottom)) {
            other()
            Text("%02d:%02d".format(weight.date.hour, weight.date.minute))
        }
    }
}

@Composable
fun WeightCardContentInfo(weight: Weight, modifier: Modifier = Modifier) {
    Text("%s: %.2f %s"
        .format(stringResource(R.string.weight_capital), weight.value, stringResource(R.string.kg)),
        modifier = modifier)
}
