package ru.foodcare.foodcare.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.foodcare.foodcare.presentation.input.InputProduct
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.product.Product.Companion.UnitType
import ru.foodcare.foodcare.presentation.viewModel.ProductViewModel

@Composable
fun ProductEditWindow(viewModel: ProductViewModel, close: () -> Unit) {
    val product = remember {viewModel.productObserved}

    CardSurface(
        Modifier
            .fillMaxSize()
            .padding(bottom = 15.dp)
            .padding(5.dp)
            .background(Color.Transparent)
            .padding(10.dp)
    ) {
        ProductEditCard(product, viewModel, close)
    }
}

@Composable
fun ProductEditCard(oldProduct: Product?, viewModel: ProductViewModel, close: () -> Unit) {
    val name = remember { mutableStateOf(oldProduct?.name ?: "") }
    val production = remember { mutableStateOf(oldProduct?.production ?: "") }
    val type = remember { mutableStateOf(UnitType.Piece) }
    val calories = remember { mutableStateOf(oldProduct?.calories?.toString() ?: "") }
    val protein = remember { mutableStateOf(oldProduct?.protein?.toString() ?: "") }
    val fat = remember { mutableStateOf(oldProduct?.fat?.toString() ?: "") }
    val carbohydrates = remember { mutableStateOf(oldProduct?.carbohydrates?.toString() ?: "") }
    val fiber = remember { mutableStateOf(oldProduct?.fiber?.toString() ?: "") }

    LazyColumn {
        itemWithUnderLine {NameEdit(name)}
        itemWithUnderLine {ProductionEdit(production)}
        itemWithUnderLine {CaloriesEdit(calories)}
        itemWithUnderLine {ProteinEdit(protein)}
        itemWithUnderLine {FatEdit(fat)}
        itemWithUnderLine {CarbohydratesEdit(carbohydrates)}
        itemWithUnderLine {FiberEdit(fiber)}
        itemWithUnderLine {ProductTypeSelector(type)}
        item {
            val caloriesNum = parseOrNull(calories.value)
            val proteinNum = parseOrNull(protein.value)
            val fatNum = parseOrNull(fat.value)
            val carbohydratesNum = parseOrNull(carbohydrates.value)
            val fiberNum = parseOrNull(fiber.value)

            SaveProductButton(oldProduct, InputProduct(name.value, production.value,
                if (type.value == UnitType.Piece) 1 else 100, type.value,
                caloriesNum, proteinNum, fatNum, carbohydratesNum,
                fiberNum),
                viewModel, close)
        }
    }
}



private fun parseOrNull(str: String): Double? {
    return str.replace(',', '.').trim().toDoubleOrNull()
}

@Composable
fun NameEdit(name: MutableState<String>) {
    ProductTextField(name.value, {name.value = it},
        prefix = {Text("Название: ")},
        placeholder = {Text("Название продукта")})
}

@Composable
fun ProductionEdit(production: MutableState<String>) {
    ProductTextField(production.value, {production.value = it},
        prefix = {Text("Производитель: ")},
        placeholder = {Text("Имя производителя")})
}

@Composable
fun CaloriesEdit(calories: MutableState<String>) {
    NutrientTextField("Калорийность: ", calories.value, "ккал"
    ) { calories.value = it }
}

@Composable
fun ProteinEdit(protein: MutableState<String>) {
    NutrientTextField("Белок: ", protein.value, "грамм"
    ) { protein.value = it }
}

@Composable
fun FatEdit(fat: MutableState<String>) {
    NutrientTextField("Жир: ", fat.value, "грамм"
    ) { fat.value = it }
}

@Composable
fun CarbohydratesEdit(carbohydrates: MutableState<String>) {
    NutrientTextField("Углеводы: ", carbohydrates.value, "грамм"
    ) { carbohydrates.value = it }
}

@Composable
fun FiberEdit(fiber: MutableState<String>) {
    NutrientTextField("Волокна: ", fiber.value, "грамм"
    ) { fiber.value = it }
}

private fun LazyListScope.itemWithUnderLine(content: @Composable (LazyItemScope.() -> Unit)) {
    item {
        content()
        HorizontalDivider(2.dp, Color.Black)
    }
}

@Composable
fun SaveProductButton(
    oldProduct: Product?,
    inputProduct: InputProduct,
    viewModel: ProductViewModel,
    close: () -> Unit
) {
    var showAlert by remember { mutableStateOf(false) }
    if (showAlert) {
        AlertWrongInput ({ WrongInputProductPreview(inputProduct) }) { showAlert = false }
    }
    TextButton({
        if (!inputProduct.isCorrect()) {
            showAlert = true
        } else {
            if (oldProduct != null) {
                viewModel.updateProduct(inputProduct.toProduct(), oldProduct)
            } else {
                viewModel.addProduct(inputProduct.toProduct())
            }
            close()
        }
    }, modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
        colors = ButtonColors(Color.Transparent, Color.Black,
            Color.Transparent, Color.Transparent)) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Save, "")
            Text("Сохранить", fontSize = 20.sp)
        }
    }
}

@Composable
fun WrongInputProductPreview(inputProduct: InputProduct) {
    Column {
        if (inputProduct.name.isEmpty()) Text("Имя не указано")
        if (inputProduct.production.isEmpty()) Text("Производитель не указан")
        if (inputProduct.calories == null) Text("Ошибка в калориях")
        if (inputProduct.protein == null) Text("Ошибка в белках")
        if (inputProduct.fat == null) Text("Ошибка в жирах")
        if (inputProduct.carbohydrates == null) Text("Ошибка в углеводах")
        if (inputProduct.fiber == null) Text("Ошибка в волокнах")
    }
}

@Composable
fun AlertWrongInput(preview: @Composable (() -> Unit) = {}, closeAlert: () -> Unit) {
    AlertDialog(closeAlert,
        {TextButton(closeAlert) {Text("Исправлю")} },
        modifier = Modifier.fillMaxWidth(),
        title = {Text("В вводе ошибки")},
        text = preview)
}

@Composable
fun ProductTypeSelector(type: MutableState<UnitType>) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 7.dp, vertical = 5.dp)
        .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        Text("Кол-во:")
        Column (verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(90.dp)) {
            ProductTypeSelectRow("1 шт - на 1 предмет", type.value == UnitType.Piece) {
                type.value = UnitType.Piece
            }
            ProductTypeSelectRow("100 г - на 100 грамм", type.value == UnitType.Gram) {
                type.value = UnitType.Gram
            }
            ProductTypeSelectRow("100 мл - на 0.1 литра", type.value == UnitType.Milliliter) {
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
        .selectable(selected) {
            onSelect()
        }) {
        CheckCircle(selected)
        Text(type)
    }
}

@Composable
fun NutrientTextField(description: String, value: String, type: String,
                      onValueChange: (String) -> Unit) {
    ProductTextField((value), onValueChange,
        placeholder = {Text("Позволены символы: [0-9 и .]")},
        prefix = {Text(description)},
        suffix = {Text(type)})
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
            .height(80.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
