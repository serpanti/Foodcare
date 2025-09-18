package ru.foodcare.foodcare.ui.composable.nutrients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.domain.nutrientsProperties.NutrientsProperties
import ru.foodcare.foodcare.ui.composable.ElementWithHeader
import ru.foodcare.foodcare.ui.composable.HorizontalDivider
import ru.foodcare.foodcare.ui.composable.VerticalDivider
import androidx.compose.ui.platform.LocalResources

@Composable
fun Nutrients(nutrientsProperties: NutrientsProperties, modifier: Modifier = Modifier) {
    val res = LocalResources.current

    Column(modifier) {
        NutrientsHorizontalDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween) {
            NutrientsVerticalDivider()
            Nutrient(stringResource(R.string.caloric_content),
                nutrientsProperties.calories.toInt(), stringResource(R.string.calories_short))
            NutrientsVerticalDivider()
            Nutrient(stringResource(R.string.protein_short_capital),
                nutrientsProperties.protein,
                res.getQuantityString(R.plurals.weight_in_grams,
                    nutrientsProperties.protein.toInt()))
            NutrientsVerticalDivider()
            Nutrient(stringResource(R.string.fat_short_capital),
                nutrientsProperties.fat, res.getQuantityString(R.plurals.weight_in_grams,
                    nutrientsProperties.fat.toInt()))
            NutrientsVerticalDivider()
            Nutrient(stringResource(R.string.carbohydrates_short_capital),
                nutrientsProperties.carbohydrates, res.getQuantityString(R.plurals.weight_in_grams,
                    nutrientsProperties.carbohydrates.toInt()))
            NutrientsVerticalDivider()
            Nutrient(stringResource(R.string.fibers_capital),
                nutrientsProperties.fiber, res.getQuantityString(R.plurals.weight_in_grams,
                    nutrientsProperties.fiber.toInt()))
            NutrientsVerticalDivider()
        }
        NutrientsHorizontalDivider()
    }
}

@Composable
fun Nutrient(description: String, value: Double, type: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(description)
        Text("%.2f".format(value))
        Text(type)
    }
}

@Composable
fun Nutrient(description: String, value: Int, type: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(description)
        Text("$value")
        Text(type)
    }
}

@Composable
fun NutrientsHorizontalDivider() {
    HorizontalDivider(2.dp, Color.Gray)
}

@Composable
fun NutrientsVerticalDivider() {
    VerticalDivider(2.dp, Color.Gray)
}

@Composable
fun NutrientsPropertiesCard(nutrientsProperties: NutrientsProperties,
                            modifier: Modifier = Modifier) {
    ElementWithHeader(header = {
        Text(stringResource(R.string.nutrients_sum_label), fontSize = 24.sp)
    }, modifier = modifier) {
        Nutrients(nutrientsProperties, modifier)
    }
}
