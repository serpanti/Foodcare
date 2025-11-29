package ru.foodcare.foodcare.ui.composable.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.ui.input.InputProduct
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.product.Product.Companion.UnitType
import ru.foodcare.foodcare.ui.composable.CheckCircle
import ru.foodcare.foodcare.ui.composable.EditCardSurface
import ru.foodcare.foodcare.ui.composable.SaveButton
import ru.foodcare.foodcare.ui.composable.parseToDoubleOrNull
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel

@Composable
fun ProductEditWindow(viewModel: ProductViewModel, close: () -> Unit) {
    val product = viewModel.lastChosenProduct.collectAsState().value

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        EditCardSurface {
            ProductEditCard(product, viewModel, close)
        }
    }
}

@Composable
fun ProductEditCard(oldProduct: Product?, viewModel: ProductViewModel, close: () -> Unit) {
    val name = rememberSaveable { mutableStateOf(oldProduct?.name ?: "") }
    val production = rememberSaveable { mutableStateOf(oldProduct?.production ?: "") }
    val type = rememberSaveable { mutableStateOf(UnitType.Piece) }
    val calories = rememberSaveable {
        mutableStateOf(oldProduct?.nutrientsProperties?.calories?.toString() ?: "")
    }
    val protein = rememberSaveable {
        mutableStateOf(oldProduct?.nutrientsProperties?.protein?.toString() ?: "")
    }
    val fat = rememberSaveable {
        mutableStateOf(oldProduct?.nutrientsProperties?.fat?.toString() ?: "")
    }
    val carbohydrates = rememberSaveable {
        mutableStateOf(oldProduct?.nutrientsProperties?.carbohydrates?.toString() ?: "")
    }
    val fiber = rememberSaveable {
        mutableStateOf(oldProduct?.nutrientsProperties?.fiber?.toString() ?: "")
    }

    Column {
        NameEdit(name)
        ProductionEdit(production)
        CaloriesEdit(calories)
        ProteinEdit(protein)
        FatEdit(fat)
        CarbohydratesEdit(carbohydrates)
        FiberEdit(fiber)
        ProductTypeSelector(type)

        val caloriesNum = calories.value.parseToDoubleOrNull()
        val proteinNum = protein.value.parseToDoubleOrNull()
        val fatNum = fat.value.parseToDoubleOrNull()
        val carbohydratesNum = carbohydrates.value.parseToDoubleOrNull()
        val fiberNum = fiber.value.parseToDoubleOrNull()
        val inputProduct = InputProduct(name.value, production.value,
            if (type.value == UnitType.Piece) 1 else 100, type.value,
            caloriesNum, proteinNum, fatNum, carbohydratesNum,
            fiberNum)

        SaveButton(
            oldProduct == null, inputProduct, close,
            add = { viewModel.addProduct(inputProduct.toProduct()) },
            update = {
                oldProduct?.let { viewModel.updateProduct(inputProduct.toProduct(), it) }
            }) {
            WrongInputProductPreview(inputProduct)
        }
    }
}

@Composable
fun NameEdit(name: MutableState<String>) {
    ProductTextField(name.value, {name.value = it},
        prefix = {Text(stringResource(R.string.product_name) + ": ")},
        placeholder = {Text(stringResource(R.string.product_name_edit_placeholder))})
}

@Composable
fun ProductionEdit(production: MutableState<String>) {
    ProductTextField(production.value, {production.value = it},
        prefix = {Text(stringResource(R.string.product_production_capital) + ": ")},
        placeholder = {Text(stringResource(R.string.product_production_capital))})
}

@Composable
fun CaloriesEdit(calories: MutableState<String>) {
    NutrientTextField(stringResource(R.string.caloric_content) +
            ": ", calories.value, stringResource(R.string.calories_short)
    ) { calories.value = it }
}

@Composable
fun ProteinEdit(protein: MutableState<String>) {
    val proteinNumber = protein.value.toDoubleOrNull()
    val gramString = LocalResources.current
        .getQuantityString(R.plurals.weight_in_grams, proteinNumber?.toInt() ?: 1)
    NutrientTextField(stringResource(R.string.protein_capital) +
            ": ", protein.value, gramString
    ) { protein.value = it }
}

@Composable
fun FatEdit(fat: MutableState<String>) {
    val fatNumber = fat.value.toDoubleOrNull()
    val gramString = LocalResources.current
        .getQuantityString(R.plurals.weight_in_grams, fatNumber?.toInt() ?: 1)
    NutrientTextField(stringResource(R.string.fat_capital) +
            ": ", fat.value, gramString
    ) { fat.value = it }
}

@Composable
fun CarbohydratesEdit(carbohydrates: MutableState<String>) {
    val carbohydratesNumber = carbohydrates.value.toDoubleOrNull()
    val gramString = LocalResources.current
        .getQuantityString(R.plurals.weight_in_grams, carbohydratesNumber?.toInt() ?: 1)
    NutrientTextField(stringResource(R.string.carbohydrates_capital) +
            ": ", carbohydrates.value, gramString
    ) { carbohydrates.value = it }
}

@Composable
fun FiberEdit(fiber: MutableState<String>) {
    val fiberNumber = fiber.value.toDoubleOrNull()
    val gramString = LocalResources.current
        .getQuantityString(R.plurals.weight_in_grams, fiberNumber?.toInt() ?: 1)
    NutrientTextField(stringResource(R.string.fibers_capital) +
            ": ", fiber.value, gramString
    ) { fiber.value = it }
}

@Composable
fun WrongInputProductPreview(inputProduct: InputProduct) {
    Column {
        if (inputProduct.name.isEmpty()) Text(stringResource(R.string.product_name_error_message))
        if (inputProduct.production.isEmpty()) Text(
            stringResource(R.string.product_production_error_message)
        )
        if (inputProduct.calories == null) Text(stringResource(R.string.energy_error_message))
        if (inputProduct.protein == null) Text(stringResource(R.string.protein_error_message))
        if (inputProduct.fat == null) Text(stringResource(R.string.fat_error_message))
        if (inputProduct.carbohydrates == null) Text(
            stringResource(R.string.carbohydrates_error_message)
        )
        if (inputProduct.fiber == null) Text(stringResource(R.string.fibers_error_message))
    }
}

@Composable
fun ProductTypeSelector(type: MutableState<UnitType>) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 15.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.quantity_capital) + ":")
        Column (verticalArrangement = Arrangement.SpaceBetween) {
            ProductTypeSelectRow(stringResource(R.string.piece_selector_text),
                type.value == UnitType.Piece) {
                type.value = UnitType.Piece
            }
            ProductTypeSelectRow(stringResource(R.string.gram_selector_text),
                type.value == UnitType.Gram) {
                type.value = UnitType.Gram
            }
            ProductTypeSelectRow(stringResource(R.string.milliliter_selector_text),
                type.value == UnitType.Milliliter) {
                type.value = UnitType.Milliliter
            }
        }
    }
}

@Composable
fun ProductTypeSelectRow(type: String, selected: Boolean, onSelect: () -> Unit) {
    Row (modifier = Modifier
        .padding(start = 50.dp)
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.small)
        .selectable(selected) {
            onSelect()
        }
        .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,) {
        CheckCircle(selected)
        Text(type)
    }
}

@Composable
fun NutrientTextField(description: String, value: String, type: String,
                      onValueChange: (String) -> Unit) {
    ProductTextField((value), onValueChange,
        placeholder = {Text("[0-9 ${stringResource(R.string.and)} .]")},
        prefix = {Text(description)},
        suffix = {Text(type)}
    )
}

@Composable
fun ProductTextField(startText: String, onValueChange: (String) -> Unit,
                     modifier: Modifier = Modifier,
                     placeholder: @Composable () -> Unit = {},
                     prefix: @Composable () -> Unit = {},
                     suffix: @Composable () -> Unit = {}) {
    TextField(startText, onValueChange,
        placeholder = placeholder,
        prefix = prefix,
        suffix = suffix,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 10.dp)
    )
}