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
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.domain.nutrientsProperties.NutrientsProperties
import ru.foodcare.foodcare.ui.composable.HorizontalDivider
import ru.foodcare.foodcare.ui.composable.VerticalDivider

@Composable
fun Nutrients(nutrientsProperties: NutrientsProperties, modifier: Modifier = Modifier) {
    Column(modifier) {
        NutrientsHorizontalDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween) {
            NutrientsVerticalDivider()
            Nutrient("Ценность", nutrientsProperties.calories, "ккал")
            NutrientsVerticalDivider()
            Nutrient("Б", nutrientsProperties.protein, "грамм")
            NutrientsVerticalDivider()
            Nutrient("Ж", nutrientsProperties.fat, "грамм")
            NutrientsVerticalDivider()
            Nutrient("У", nutrientsProperties.carbohydrates, "грамм")
            NutrientsVerticalDivider()
            Nutrient("Волокна", nutrientsProperties.fiber, "грамм")
            NutrientsVerticalDivider()
        }
        NutrientsHorizontalDivider()
    }
}

@Composable
fun Nutrient(description: String, value: Double, type: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(description)
        Text(value.toString())
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