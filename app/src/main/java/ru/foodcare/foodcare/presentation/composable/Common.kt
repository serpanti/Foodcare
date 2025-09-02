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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.product.Product.Companion.UnitType
import ru.foodcare.foodcare.presentation.input.Input

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
fun FloatingAddButton(modifier: Modifier = Modifier, openEditor: () -> Unit = {}) {
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
                lazyListState.animateScrollToItem(0)
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
                modifier: Modifier = Modifier,
                isOpened: () -> Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier.offset(y = 2.dp).padding(5.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .padding(5.dp)) {
        SearchTextField(startValue, onValueChange, Modifier.weight(1f))
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
        modifier = modifier.padding(start = 5.dp)
    )
}

@Composable
fun SimpleTextField(value: String, onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier,
                    placeholder: @Composable () -> Unit,
                    prefix: @Composable () -> Unit = {},
                    suffix: @Composable () -> Unit = {}) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        decorationBox = @Composable { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()) {
                prefix()
                if (value.isEmpty()) {
                    placeholder()
                } else {
                    innerTextField()
                }
                suffix()
            }
        }
    )
}

@Composable
fun SquareButton(value: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    CustomButton(value, modifier.size(40.dp), onClick)
}

@Composable
fun CustomButton(value: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(5.dp)
    Box(modifier = modifier.padding(5.dp)
        .shadow(5.dp, shape, clip = true)
        .border(2.dp, Color.Black, shape)
        .clip(shape)
        .background(Color.White)
        .clickable{onClick()}
        .padding(5.dp)) {
        Text(value, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun Dp.withLayoutDirection(): Dp {
    return if (LocalLayoutDirection.current == LayoutDirection.Rtl) this * -1 else this
}

@Composable
fun AdviceButton(advice: String, onCLick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        TextButton(onCLick,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(advice)
        }
    }
}

@Composable
fun ElementWithHeader(header: @Composable () -> Unit, modifier: Modifier = Modifier,
                      content: @Composable () -> Unit) {
    Column(modifier = modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        header()
        content()
    }
}

@Composable
fun Header(text: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
        contentAlignment = Alignment.Center) {
        Text(text, fontSize = 32.sp)
    }
}

fun LazyListScope.itemWithUnderLine(content: @Composable (LazyItemScope.() -> Unit)) {
    item {
        content()
        HorizontalDivider(2.dp, Color.Black)
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

fun String.parseToDoubleOrNull(): Double? {
    return replace(',', '.').trim().toDoubleOrNull()
}

fun String.parseToIntOrNull(): Int? {
    return trim().toIntOrNull()
}

@Composable
fun SaveButton(
    isNewRecord: Boolean,
    input: Input,
    close: () -> Unit,
    add: () -> Unit,
    update: () -> Unit,
    alertMessage: @Composable () -> Unit
) {
    var showAlert by remember { mutableStateOf(false) }
    if (showAlert) {
        AlertWrongInput (alertMessage) { showAlert = false }
    }
    TextButton({
        if (!input.isCorrect()) {
            showAlert = true
        } else {
            if (isNewRecord) {
                add()
            } else {
                update()
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
fun UnitType.toStringWithLanguage(): String {
    // TODO интеграция с resources
    return when (this) {
        UnitType.Milliliter -> "мл"
        UnitType.Gram -> "г"
        UnitType.Piece -> "шт"
    }
}

@Composable
fun IconWithAction(action: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.clickable(onClick = action), contentAlignment = Alignment.Center) {
        Icon(Icons.Filled.Delete, "Удалить запись")
    }
}

@Composable
fun AlertAboutActionDialog(close: () -> Unit, action: () -> Unit, question: String,
                           preview: @Composable () -> Unit = {}) {
    AlertDialog (
        onDismissRequest = close,
        dismissButton = {TextButton(close) {
            Text("Отмена")
        }},
        confirmButton = {TextButton({
            action()
            close()
        }) { Text("Подтвердить") }},
        title = {
            Text(question)
        },
        text = preview,
    )
}

@Composable
fun AlertDeleteDialog(close: () -> Unit, delete: () -> Unit,
                      preview: @Composable () -> Unit = {}) {
    AlertAboutActionDialog(close, delete, "Вы уверены, что хотите удалить?", preview)
}

@Composable
fun InfinitePager(modifier: Modifier = Modifier,
                  increase: () -> Unit,
                  decrease: () -> Unit,
                  content: @Composable PagerScope.(Int) -> Unit) {
    val center = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(initialPage = center) { Int.MAX_VALUE }
    var lastPage by remember{ mutableIntStateOf(pagerState.currentPage) }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                when {
                    lastPage < currentPage -> increase()
                    lastPage > currentPage -> decrease()
                }

                if (currentPage == 0 || currentPage == Int.MAX_VALUE) {
                    lastPage = center
                    pagerState.scrollToPage(center)
                } else {
                    lastPage = currentPage
                }
            }
    }

    HorizontalPager(pagerState, modifier = modifier) { page ->
        content(page)
    }
}
