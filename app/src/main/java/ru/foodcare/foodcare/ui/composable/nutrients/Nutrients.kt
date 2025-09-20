package ru.foodcare.foodcare.ui.composable.nutrients

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.domain.nutrientsProperties.NutrientsProperties
import ru.foodcare.foodcare.ui.composable.ElementWithHeader
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp

@Composable
fun Nutrients(nutrientsProperties: NutrientsProperties, modifier: Modifier = Modifier) {
    val res = LocalResources.current

    Column(modifier = modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.height(IntrinsicSize.Max).fillMaxWidth()) {
            Nutrient(stringResource(R.string.protein_short_capital),
                nutrientsProperties.protein,
                res.getQuantityString(R.plurals.weight_in_grams,
                    nutrientsProperties.protein.toInt()),
                modifier = Modifier.weight(1f))
            Nutrient(stringResource(R.string.fat_short_capital),
                nutrientsProperties.fat, res.getQuantityString(R.plurals.weight_in_grams,
                    nutrientsProperties.fat.toInt()),
                modifier = Modifier.weight(1f))
            Nutrient(stringResource(R.string.carbohydrates_short_capital),
                nutrientsProperties.carbohydrates, res.getQuantityString(R.plurals.weight_in_grams,
                    nutrientsProperties.carbohydrates.toInt()),
                modifier = Modifier.weight(1f))
        }
        Row(Modifier.height(IntrinsicSize.Max).fillMaxWidth()) {
            Nutrient(stringResource(R.string.caloric_content),
                nutrientsProperties.calories.toInt(), stringResource(R.string.calories_short),
                modifier = Modifier.weight(1f))
            Nutrient(stringResource(R.string.fibers_capital),
                nutrientsProperties.fiber, res.getQuantityString(R.plurals.weight_in_grams,
                    nutrientsProperties.fiber.toInt()),
                modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun Nutrient(description: String, value: Double, type: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .padding(10.dp)) {
        Text(description)
        Text("%.2f".format(value))
        Text(type)
    }
}

@Composable
fun Nutrient(description: String, value: Int, type: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .padding(10.dp)) {
        Text(description)
        Text("$value")
        Text(type)
    }
}

@Composable
fun NutrientsPropertiesCard(nutrientsProperties: NutrientsProperties,
                            modifier: Modifier = Modifier) {
    ElementWithHeader(header = {
        Text(stringResource(R.string.nutrients_sum_label),
            style = MaterialTheme.typography.titleLarge)
    }, modifier = modifier) {
        Nutrients(nutrientsProperties, modifier)
    }
}
