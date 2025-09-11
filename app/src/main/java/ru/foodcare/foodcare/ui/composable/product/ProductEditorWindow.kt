package ru.foodcare.foodcare.ui.composable.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.ui.input.InputProduct
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.product.Product.Companion.UnitType
import ru.foodcare.foodcare.ui.composable.CardSurface
import ru.foodcare.foodcare.ui.composable.CheckCircle
import ru.foodcare.foodcare.ui.composable.SaveButton
import ru.foodcare.foodcare.ui.composable.itemWithUnderLine
import ru.foodcare.foodcare.ui.composable.parseToDoubleOrNull
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel

@Composable
fun ProductEditWindow(viewModel: ProductViewModel, close: () -> Unit) {
    val product = viewModel.lastChosenProduct.collectAsState().value

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
