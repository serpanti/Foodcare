package ru.foodcare.foodcare.ui.composable.meal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.product.Product.Companion.UnitType
import ru.foodcare.foodcare.ui.composable.CardSurface
import ru.foodcare.foodcare.ui.composable.HorizontalDivider
import ru.foodcare.foodcare.ui.composable.SaveButton
import ru.foodcare.foodcare.ui.composable.product.SearchProductFieldWithList
import ru.foodcare.foodcare.ui.composable.SimpleTextField
import ru.foodcare.foodcare.ui.composable.TimeEdit
import ru.foodcare.foodcare.ui.composable.parseToIntOrNull
import ru.foodcare.foodcare.ui.composable.toStringWithLanguage
import ru.foodcare.foodcare.ui.input.InputMeal
import ru.foodcare.foodcare.ui.viewModel.meal.MealViewModel
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel
import java.time.LocalDateTime

@Composable
fun MealEditWindow(viewModel: MealViewModel, productViewModel: ProductViewModel,
                   close: () -> Unit) {
    val meal = viewModel.mealObserved.collectAsState()

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        CardSurface(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
                .padding(5.dp)
                .background(Color.Transparent)
                .padding(10.dp)
        ) {
            MealEditCard(meal.value, viewModel, productViewModel, close)
        }
    }
}

@Composable
fun MealEditCard(oldMeal: Meal?, viewModel: MealViewModel,
                 productViewModel: ProductViewModel, close: () -> Unit) {
    val id = rememberSaveable { oldMeal?.id ?: 0 }
    val product = rememberSaveable { mutableStateOf<Product?>(oldMeal?.product) }
    val productCount = rememberSaveable {
        mutableStateOf(oldMeal?.product?.amount?.toString() ?: "")
    }
    val year = rememberSaveable { mutableStateOf(oldMeal?.date?.year?.toString() ?: "") }
    val month = rememberSaveable { mutableStateOf(oldMeal?.date?.monthValue?.toString() ?: "") }
    val day = rememberSaveable { mutableStateOf(oldMeal?.date?.dayOfMonth?.toString() ?: "") }
    val hours = rememberSaveable { mutableStateOf(oldMeal?.date?.hour?.toString() ?: "") }
    val minutes = rememberSaveable { mutableStateOf(oldMeal?.date?.minute?.toString() ?: "") }
    val seconds = rememberSaveable { mutableStateOf(oldMeal?.date?.second?.toString() ?: "") }

    Column {
        ProductEdit(product, productCount, productViewModel)
        HorizontalDivider(2.dp, Color.Black)
        TimeEdit(year, month, day, hours, minutes, seconds,
            Modifier.padding(bottom = 5.dp))
        HorizontalDivider(2.dp, Color.Black)

        val productCountNum = productCount.value.parseToIntOrNull()
        val yearNum = year.value.parseToIntOrNull()
        val monthNum = month.value.parseToIntOrNull()
        val dayNum = day.value.parseToIntOrNull()
        val hoursNum = hours.value.parseToIntOrNull()
        val minutesNum = minutes.value.parseToIntOrNull()
        val secondsNum = seconds.value.parseToIntOrNull()
        val inputMeal = InputMeal(id, product.value, productCountNum,
            yearNum, monthNum, dayNum, hoursNum, minutesNum, secondsNum)

        SaveButton(
            oldMeal == null, inputMeal, close,
            { viewModel.addMeal(inputMeal.toMeal()) },
            { viewModel.updateMeal(inputMeal.toMeal()) }) {
            WrongInputMealPreview(inputMeal)
        }
    }
}

@Composable
fun ProductEdit(
    product: MutableState<Product?>,
    productCount: MutableState<String>,
    productViewModel: ProductViewModel
) {
    val selectedProductDelegate = remember { derivedStateOf { product.value } }
    val productConst = selectedProductDelegate.value

    Column(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth()) {
            SelectedProductText(productConst, Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp).padding(top = 10.dp))

            SearchProductFieldWithList(productViewModel, Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp).padding(vertical = 5.dp)
                , product)
        }

        if (productConst != null) {
            ProductCountTextField(productCount, productConst.type)
        }
    }
}

@Composable
fun ProductCountTextField(productCount: MutableState<String>, type :UnitType,
                          modifier: Modifier = Modifier) {
    SimpleTextField(productCount.value, onValueChange = { newStr ->
        productCount.value = newStr
    }, modifier = modifier.padding(10.dp), placeholder = {
        Text(stringResource(R.string.quantity_input_product_instruction))
    }, prefix = {
        Text("${stringResource(R.string.quantity_capital)}: ")
    }, suffix = {
        Text(type.toStringWithLanguage())
    })
}

@Composable
fun SelectedProductText(product: Product?, modifier: Modifier = Modifier) {
    val innerModifier = modifier
        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .padding(10.dp)
    val text = if (product != null) {
        "%s %s".format(product.name, product.production)
    } else {
        stringResource(R.string.select_product_status)
    }
    Text(text, innerModifier)
}

@Composable
fun WrongInputMealPreview(inputMeal: InputMeal) {
    Column {
        if (inputMeal.product == null) Text(stringResource(R.string.product_error_message))
        if (inputMeal.year == null) Text(stringResource(R.string.year_error_message))
        if (inputMeal.month == null) Text(stringResource(R.string.month_error_message))
        if (inputMeal.day == null) Text(stringResource(R.string.day_error_message))
        if (inputMeal.hours == null) Text(stringResource(R.string.hours_error_message))
        if (inputMeal.minutes == null) Text(stringResource(R.string.minutes_error_message))
        if (inputMeal.seconds == null) Text(stringResource(R.string.seconds_error_message))
        if (inputMeal.productCount == null) Text(stringResource(R.string.product_quantity_error_message))
        if (inputMeal.year != null && inputMeal.month != null && inputMeal.day != null &&
            inputMeal.hours != null && inputMeal.minutes != null && inputMeal.seconds != null) {
                try {
                    LocalDateTime.of(inputMeal.year, inputMeal.month, inputMeal.day,
                        inputMeal.hours, inputMeal.minutes, inputMeal.seconds)
                } catch (_: Exception) {
                    Text(stringResource(R.string.wrong_date_message))
                }
            }
    }
}
