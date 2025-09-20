package ru.foodcare.foodcare.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.R
import ru.foodcare.foodcare.domain.product.Product.Companion.UnitType
import ru.foodcare.foodcare.ui.input.Input
import java.time.LocalDate
import java.time.LocalDateTime

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
    FloatingActionButton(modifier, openEditor)
}

@Composable
fun FloatingAddListButton(modifier: Modifier = Modifier,
                          editList: List<(@Composable () -> Unit)> = emptyList()) {
    var isOpened by remember { mutableStateOf(false) }

    FloatingActionButton(modifier,
        action = {isOpened = !isOpened},
        contentDescription = stringResource(R.string.open_the_add_menu),
        rotateAngle = 45f,
        editList = editList,
        listIsOpened = isOpened
    )
}

@Composable
fun FloatingActionButton(modifier: Modifier = Modifier, action: () -> Unit = {},
                         rotateAngle: Float = 0f, contentDescription: String? = null,
                                 editList: List<(@Composable () -> Unit)> = emptyList(),
                                 listIsOpened: Boolean = false) {
    val transition = updateTransition(targetState = listIsOpened,
        label = "floatingActionButtonTransition")

    val rotate by transition.animateFloat (
        transitionSpec = {
            tween(
                durationMillis = 600,
                easing = LinearOutSlowInEasing
            )
        },
        label = "floatingActionButtonRotationAnimation"
    ) { state ->
        if (state) rotateAngle else 0f
    }

    Column (modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {
        editList.forEachIndexed { idx, editBlock ->
            val targetOffset = (70 + 30 * (editList.lastIndex - idx))

            val offset by transition.animateDp(
                transitionSpec = {
                    tween(600, easing = FastOutSlowInEasing)
                },
                label = "floatingActionButtonEditListItem${idx}Animation"
            ) { state ->
                if (state) 0.dp else targetOffset.dp
            }

            Box(modifier = Modifier.offset(y = offset)) {
                editBlock()
            }
        }

        val shape = CircleShape

        Box(modifier = Modifier
            .size(57.dp)
            .clip(shape)
            .shadow(
                10.dp,
                shape = shape,
                clip = false
            )
            .background(Color.LightGray)
            .zIndex(1f)
            .rotate(rotate)
            .clickable(onClick = action)
            .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Add, contentDescription,
                modifier = Modifier.fillMaxSize())
        }
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
        }) { Icon(Icons.Filled.KeyboardArrowUp, stringResource(R.string.move_to_top)) }
    }
}

@Composable
fun EditMenu(visibleState: MutableState<Boolean>,
             edit: () -> Unit, remove: () -> Unit, modifier: Modifier = Modifier, offset: DpOffset) {
    DropdownMenu(visibleState.value, {visibleState.value = false}, modifier = modifier,
        offset = offset) {
        DropdownMenuItem({
            Text(stringResource(R.string.edit))
        }, {
            visibleState.value = false
            edit()
        }, trailingIcon = {Icon(Icons.Filled.Edit, null)})
        HorizontalDivider(1.dp, Color.Gray)
        DropdownMenuItem({
            Text(stringResource(R.string.delete))
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
        modifier = modifier
            .offset(y = 2.dp)
            .padding(5.dp)
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
                Icon(Icons.Filled.Close, stringResource(R.string.close_search))
            } else {
                Icon(Icons.Filled.Search, stringResource(R.string.open_search))
            }
        }
    }
}

@Composable
fun SearchTextField(startText: String, onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier) {
    SimpleTextField(startText, onValueChange,
        placeholder = {Text(stringResource(R.string.search))},
        modifier = modifier.padding(start = 5.dp)
    )
}

@Composable
fun SimpleTextField(value: String, onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier,
                    placeholder: @Composable () -> Unit,
                    prefix: @Composable () -> Unit = {},
                    suffix: @Composable () -> Unit = {}) {
    var hasFocus by remember {mutableStateOf(false)}

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.onFocusChanged { focusState ->
            hasFocus = focusState.hasFocus
        },
        decorationBox = @Composable { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()) {
                prefix()
                if (value.isEmpty() && !hasFocus) {
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
fun SquareButton(value: String, modifier: Modifier = Modifier,
                 backgroundColor: Color = Color.White, onClick: () -> Unit) {
    CustomButton(value, modifier.size(40.dp), backgroundColor, onClick = onClick)
}

@Composable
fun CustomButton(value: String, modifier: Modifier = Modifier,
                 backgroundColor: Color = Color.White, onClick: () -> Unit) {
    val shape = RoundedCornerShape(5.dp)
    Box(modifier = modifier
        .padding(5.dp)
        .shadow(5.dp, shape, clip = true)
        .border(2.dp, Color.Black, shape)
        .clip(shape)
        .background(backgroundColor)
        .clickable { onClick() }
        .padding(5.dp)) {
        Text(value, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun Dp.withLayoutDirection(): Dp {
    return if (LocalLayoutDirection.current == LayoutDirection.Rtl) this * -1 else this
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

@Composable
fun AlertWrongInput(preview: @Composable (() -> Unit) = {}, closeAlert: () -> Unit) {
    AlertDialog(closeAlert,
        {TextButton(closeAlert) {Text(stringResource(R.string.fix))} },
        modifier = Modifier.fillMaxWidth(),
        title = {Text(stringResource(R.string.error_in_the_input))},
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
    modifier: Modifier = Modifier,
    alertMessage: @Composable () -> Unit
) {
    var showAlert by remember { mutableStateOf(false) }
    if (showAlert) {
        AlertWrongInput (alertMessage) { showAlert = false }
    }
    Box(modifier = modifier
        .fillMaxWidth()
        .height(50.dp)
        .clickable {
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
        }) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Save, "")
            Text(stringResource(R.string.save), fontSize = 20.sp)
        }
    }
}

@Composable
fun UnitType.toStringWithLanguage(): String {
    return when (this) {
        UnitType.Milliliter -> stringResource(R.string.ml)
        UnitType.Gram -> stringResource(R.string.g)
        UnitType.Piece -> stringResource(R.string.piece)
    }
}

@Composable
fun IconWithAction(action: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.clickable(onClick = action), contentAlignment = Alignment.Center) {
        Icon(Icons.Filled.Delete, stringResource(R.string.delete_record))
    }
}

@Composable
fun AlertAboutActionDialog(close: () -> Unit, action: () -> Unit, question: String,
                           preview: @Composable () -> Unit = {}) {
    AlertDialog (
        onDismissRequest = close,
        dismissButton = {TextButton(close) {
            Text(stringResource(R.string.cancel))
        }},
        confirmButton = {TextButton({
            action()
            close()
        }) { Text(stringResource(R.string.ok)) }},
        title = {
            Text(question)
        },
        text = preview,
    )
}

@Composable
fun AlertDeleteDialog(close: () -> Unit, delete: () -> Unit,
                      preview: @Composable () -> Unit = {}) {
    AlertAboutActionDialog(close, delete, stringResource(R.string.delete_question), preview)
}

@Composable
fun InfiniteDatePager(modifier: Modifier = Modifier,
                      currentDate: LocalDate,
                      getChangedDate: LocalDate.(Long) -> LocalDate,
                  setNewDate: (LocalDate) -> Unit = {},
                  content: @Composable (LocalDate) -> Unit) {
    var edgeCaseKey by remember { mutableStateOf(false) }
    val fixedStartDate = remember(edgeCaseKey) { currentDate }
    val center = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(initialPage = center) { Int.MAX_VALUE }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                setNewDate(fixedStartDate.getChangedDate((page - center).toLong()))

                if (page == 0 || page == Int.MAX_VALUE) {
                    edgeCaseKey = !edgeCaseKey
                }
            }
    }

    LaunchedEffect(fixedStartDate) {
        pagerState.scrollToPage(center)
    }

    HorizontalPager(pagerState, modifier = modifier) { page ->
        val pageDate = fixedStartDate.getChangedDate((page - center).toLong())
        content(pageDate)
    }
}

@Composable
fun CommonSnackbar(data: SnackbarData) {
    val action: (@Composable () -> Unit)? = {
        TextButton({ data.performAction() }) {
            Text(data.visuals.actionLabel ?: stringResource(R.string.ok))
        }
    }

    val dismissAction: (@Composable () -> Unit)? =
        if (data.visuals.withDismissAction) {
            { TextButton({ data.dismiss() }) { Text(stringResource(R.string.cancel)) } }
        } else null

    val snackBarSize = 60.dp
    Snackbar(modifier = Modifier.height(snackBarSize)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()) {
            Text(data.visuals.message)
            Row (verticalAlignment = Alignment.CenterVertically) {
                action?.invoke()
                dismissAction?.invoke()

                val strokeWidth = 4.dp
                CommonSnackbarProgressIndicator(modifier = Modifier
                    .size(snackBarSize / 2 + strokeWidth * 2),
                    strokeWidth = strokeWidth,
                    durationMillis = when (data.visuals.duration) {
                        SnackbarDuration.Long -> 16000
                        SnackbarDuration.Short -> 4000
                        SnackbarDuration.Indefinite -> 60_000
                    }) {
                    data.dismiss()
                }
            }
        }
    }
}

@Composable
fun CommonSnackbarProgressIndicator(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 2.dp,
    durationMillis: Int,
    finishedListener: ((Float) -> Unit)? = null
) {
    val durationMillis = durationMillis
    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (started) 0f else 1f,
        animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing),
        label = "snackbarProgress",
        finishedListener = finishedListener
    )
    val timeLeft by remember { derivedStateOf { (progress * durationMillis).toInt() / 1000 } }

    LaunchedEffect(Unit) { started = true }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Text(timeLeft.toString())
        CircularProgressIndicator(progress = { progress },
            strokeWidth = strokeWidth, modifier = Modifier.matchParentSize())
    }
}

@Composable
fun blinkColorAsState(color1: Color = Color.White,
                      color2: Color = Color.Cyan,
                      durationMillis: Int = 500): MutableState<Color> {
    val animatedColor = remember { mutableStateOf(color1) }
    val transition = rememberInfiniteTransition(label = "blink")
    val fraction by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blinkFraction"
    )

    animatedColor.value = lerp(color1, color2, fraction)
    return animatedColor
}

@Composable
fun TimeEdit(
    year: MutableState<String>,
    month: MutableState<String>,
    day: MutableState<String>,
    hours: MutableState<String>,
    minutes: MutableState<String>,
    seconds: MutableState<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        CustomButton(stringResource(R.string.set_current_time), Modifier.fillMaxWidth()) {
            setSystemTime(year, month, day, hours, minutes, seconds)
        }
        val fieldModifier = Modifier.fillMaxWidth().padding(5.dp).weight(1f)
        val rowModifier = Modifier.padding(start = 10.dp).height(30.dp)
        Row(modifier = rowModifier, verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.date_capital) + ":", modifier = Modifier.weight(1f))
            YearTextField(year, modifier = fieldModifier)
            MonthTextField(month, modifier = fieldModifier)
            DayTextField(day, modifier = fieldModifier)
        }
        Row(modifier = rowModifier, verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.time_capital) + ":", modifier = Modifier.weight(1f))
            HoursTextField(hours, modifier = fieldModifier)
            MinutesTextField(minutes, modifier = fieldModifier)
            SecondsTextField(seconds, modifier = fieldModifier)
        }
    }
}

private fun setSystemTime(year: MutableState<String>,
                          month: MutableState<String>,
                          day: MutableState<String>,
                          hours: MutableState<String>,
                          minutes: MutableState<String>,
                          seconds: MutableState<String>) {
    val time = LocalDateTime.now()
    year.value = time.year.toString()
    month.value = time.monthValue.toString()
    day.value = time.dayOfMonth.toString()
    hours.value = time.hour.toString()
    minutes.value = time.minute.toString()
    seconds.value = time.second.toString()
}

@Composable
fun YearTextField(year: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(year.value, onValueChange = { newStr ->
        year.value = newStr
    }, modifier = modifier, placeholder = {
        Text("2020")
    })
}

@Composable
fun MonthTextField(month: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(month.value, onValueChange = { newStr ->
        month.value = newStr
    }, modifier = modifier, placeholder = {
        Text("(1-12)")
    })
}

@Composable
fun DayTextField(day: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(day.value, onValueChange = { newStr ->
        day.value = newStr
    }, modifier = modifier, placeholder = {
        Text("(1-31)")
    })
}

@Composable
fun HoursTextField(hour: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(hour.value, onValueChange = { newStr ->
        hour.value = newStr
    }, modifier = modifier, placeholder = {
        Text("(0-23)")
    })
}

@Composable
fun MinutesTextField(minutes: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(minutes.value, onValueChange = { newStr ->
        minutes.value = newStr
    }, modifier = modifier, placeholder = {
        Text("(0-59)")
    })
}

@Composable
fun SecondsTextField(seconds: MutableState<String>, modifier: Modifier = Modifier) {
    SimpleTextField(seconds.value, onValueChange = { newStr ->
        seconds.value = newStr
    }, modifier = modifier, placeholder = {
        Text("(0-59)")
    })
}
