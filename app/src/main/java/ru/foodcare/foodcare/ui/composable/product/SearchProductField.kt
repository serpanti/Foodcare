package ru.foodcare.foodcare.ui.composable.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.ui.composable.SearchField
import ru.foodcare.foodcare.ui.viewModel.product.ProductViewModel

@Composable
fun SearchProductField(productViewModel: ProductViewModel, modifier: Modifier = Modifier) {
    val key = productViewModel.key.collectAsState()
    val isOpened = remember { derivedStateOf { key.value.isNotEmpty() } }
    val defaultKey = ""

    val onStartSearching: () -> Unit = {}
    val onStopSearching: () -> Unit = {
        productViewModel.onKeyChanged(defaultKey)
    }
    val onValueChange: (String) -> Unit = { newKey ->
        productViewModel.onKeyChanged(newKey)
    }

    SearchField(
        onStartSearching = onStartSearching,
        onStopSearching = onStopSearching,
        startValue = key.value,
        onValueChange = onValueChange,
        modifier = modifier
    ) { isOpened.value }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductFieldWithList(
    productViewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    product: MutableState<Product?>
) {
    val key = productViewModel.key.collectAsState()
    val productsByQuery = productViewModel.productsByQuery.collectAsState()
    val isOpened = remember { derivedStateOf { key.value.isNotEmpty() } }
    val defaultKey = ""

    val onStopSearching: () -> Unit = {
        productViewModel.onKeyChanged(defaultKey)
    }
    val onValueChange: (String) -> Unit = { newKey ->
        productViewModel.onKeyChanged(newKey)
    }

    ExposedDropdownMenuBox(expanded = isOpened.value,
        onExpandedChange = {},
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)) {
        TextField(modifier = Modifier
            .fillMaxWidth()
            .menuAnchor(MenuAnchorType.PrimaryEditable),
            value = key.value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = {Text(stringResource(R.string.search))},
            colors = ExposedDropdownMenuDefaults.textFieldColors())

        ExposedDropdownMenu(expanded = isOpened.value, onDismissRequest = {}) {
            productsByQuery.value.forEach { productItem ->
                DropdownMenuItem(
                    text = { Text(productItem.name,
                        style = MaterialTheme.typography.bodyLarge)
                    }, onClick = {
                        product.value = productItem
                        onStopSearching()
                    }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding)
            }
        }
    }
}
