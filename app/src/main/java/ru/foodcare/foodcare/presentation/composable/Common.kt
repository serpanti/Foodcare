package ru.foodcare.foodcare.presentation.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun VerticalDivider(width: Dp, color: Color) {
    Box(modifier = Modifier
        .width(width)
        .fillMaxHeight()
        .background(color))
}

@Composable
fun HorizontalDivider(height: Dp, color: Color) {
    Box(modifier = Modifier
        .height(height)
        .fillMaxWidth()
        .background(color))
}

@Composable
fun CheckCircle(selected: Boolean) {
    Crossfade (selected) { isSelected ->
        Icon(if (isSelected) {
            Icons.Filled.RadioButtonChecked
        } else {
            Icons.Filled.RadioButtonUnchecked
        }, "")
    }
}

@Composable
fun CardSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier, shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color.Black), shadowElevation = 5.dp) {
        content()
    }
}

@Composable
fun AddProductButton(modifier: Modifier = Modifier, openEditor: () -> Unit = {}) {
    FloatingActionButton(openEditor,
        shape = CircleShape,
        modifier = modifier,
        containerColor = Color.LightGray) {
        Icon(Icons.Filled.AddCircle, "открыть меню добавления")
    }
}

@Composable
fun SwipeToStartButton(modifier: Modifier = Modifier, lazyListState: LazyListState) {
    val scope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 4 }
    }

    AnimatedVisibility(visible = showButton,
        modifier = modifier,
        enter = slideInVertically() + fadeIn(), exit = slideOutVertically() + fadeOut()) {
        IconButton({
            scope.launch {
                lazyListState.scrollToItem(0)
            }
        }) { Icon(Icons.Filled.KeyboardArrowUp, "Переместиться наверх") }
    }
}

@Composable
fun InfiniteLoading() {
    var indicatorSize by remember {mutableStateOf(40.dp)}
    val density = LocalDensity.current

    Box(modifier = Modifier.fillMaxSize()
        .padding(70.dp)
        .onGloballyPositioned {coordinates ->
            with(density) {
                val minSize = minOf(coordinates.size.width, coordinates.size.height).toDp()
                if (minSize != indicatorSize) indicatorSize = minSize
            }
        },
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier
            .size(indicatorSize), strokeWidth = 20.dp)
    }
}

@Composable
fun EditMenu(visibleState: MutableState<Boolean>,
             edit: () -> Unit, remove: () -> Unit, modifier: Modifier = Modifier, offset: DpOffset) {
    DropdownMenu(visibleState.value, {visibleState.value = false}, modifier = modifier,
        offset = offset) {
        DropdownMenuItem({
            Text("Редактировать")
        }, {
            visibleState.value = false
            edit()
        }, trailingIcon = {Icon(Icons.Filled.Edit, null)})
        HorizontalDivider(1.dp, Color.Gray)
        DropdownMenuItem({
            Text("Удалить")
        }, {
            visibleState.value = false
            remove()
        }, trailingIcon = {Icon(Icons.Filled.Delete, null)})
    }
}

@Composable
fun SearchField(onStartSearching: () -> Unit,
                onStopSearching: () -> Unit,
                startValue: String,
                onValueChange: (String) -> Unit,
                isOpened: () -> Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.offset(y = 2.dp).padding(5.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(15.dp)).height(55.dp)
            .padding(5.dp)) {
        if (isOpened()) {
            SearchTextField(startValue, onValueChange)
        }
        IconButton({
            if (isOpened()) onStopSearching() else onStartSearching()
        }) {
            if (isOpened()) {
                Icon(Icons.Filled.Close, "Закрыть поиск")
            } else {
                Icon(Icons.Filled.Search, "Открыть поиск")
            }
        }
    }
}

@Composable
fun SearchTextField(startText: String, onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier) {
    SimpleTextField(startText, onValueChange,
        placeholder = {Text("Поиск")},
        modifier = modifier.width(150.dp).padding(start = 5.dp)
    )
}

@Composable
fun SimpleTextField(value: String, onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier,
                    placeholder: @Composable () -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        decorationBox = @Composable { innerTextField ->
            if (value.isEmpty()) {
                placeholder()
            } else {
                innerTextField()
            }
        }
    )
}

@Composable
fun SquareButton(value: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(5.dp)
    Box(modifier = Modifier.padding(5.dp)
        .shadow(5.dp, shape, clip = true)
        .border(2.dp, Color.Black, shape)
        .clip(shape)
        .background(Color.White)
        .clickable{onClick()}
        .padding(5.dp)
        .size(40.dp)) {
        Text(value, modifier = Modifier.align(Alignment.Center))
    }
}
