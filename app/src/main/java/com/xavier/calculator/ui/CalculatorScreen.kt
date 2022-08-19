package com.xavier.calculator.ui

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xavier.calculator.R
import com.xavier.calculator.api.*

private const val TAG = "CalculatorScreen"
private const val HORIZONTAL_PADDING_VALUE = 16
private const val DURATION_MILLIS = 350
private val RESERVOIR_TEXT_STYLE: TextStyle = TextStyle.Default.copy(
    fontSize = 20.sp, /*fontWeight = FontWeight.SemiBold,*/ textAlign = TextAlign.End
)
private val NUMBER_TEXT_STYLE: TextStyle = TextStyle.Default.copy(
    fontSize = 18.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    var open by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed) {
        Log.d(TAG, "CalculatorScreen: ${it.name}")
        open = it != DrawerValue.Closed
        return@rememberDrawerState true
    }
    LaunchedEffect(key1 = open) {
        if (open) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            History(viewModel, open) { open = false }
        }, drawerState = drawerState
    ) {
        Scaffold(topBar = {
            TopBar(stringResource(id = R.string.calculate), open) {
                open = true
            }
        }) { paddingValues ->
            ContentScreen(viewModel, paddingValues)
        }
    }
}

@Composable
fun ContentScreen(viewModel: CalculatorViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        val reservoirModifier = if (isVerticalScreen) {
            Modifier
                .fillMaxSize()
                .weight(1f)
        } else {
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(1 / 4f)
        }
        Column(
            modifier = reservoirModifier, verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val textHorizontalPadding = (HORIZONTAL_PADDING_VALUE + 8).dp

            val outcome by viewModel.outcome.collectAsState()
            val completion by viewModel.completion.collectAsState()
            val formula by viewModel.formula.collectAsState()

            val standardWidth: Int = screenWidth - (textHorizontalPadding.value * 2).dpToPx.toInt()

            val scrollState = rememberScrollState()
            var textLayoutWidth by remember { mutableStateOf(0) }
            val textLayout: (TextLayoutResult) -> Unit = {
                textLayoutWidth = it.size.width
            }

            val reservoirScrollState = rememberScrollState()
            var reservoirTextLayoutWidth by remember { mutableStateOf(0) }
            val reservoirTextLayout: (TextLayoutResult) -> Unit = {
                reservoirTextLayoutWidth = it.size.width
            }

            LaunchedEffect(key1 = textLayoutWidth, key2 = reservoirTextLayoutWidth) {
                if (textLayoutWidth > standardWidth) {
                    scrollState.animateScrollTo(textLayoutWidth - standardWidth)
                }
                if (reservoirTextLayoutWidth > standardWidth) {
                    reservoirScrollState.animateScrollTo(reservoirTextLayoutWidth - standardWidth)
                }
            }

            var animationValue by remember {
                mutableStateOf(24f)
            }

            if (!completion) {
                animationValue = 24f
                Text(
                    text = formula,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(textHorizontalPadding, 0.dp)
                        .horizontalScroll(scrollState),
                    onTextLayout = textLayout,
                    style = RESERVOIR_TEXT_STYLE
                )

                if (isVerticalScreen) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .padding(HORIZONTAL_PADDING_VALUE.dp, 0.dp)
                            .background(MaterialTheme.colorScheme.outline)
                    )
                }
            }

            Log.i(TAG, "ContentScreen: $animationValue")
            if (completion) {
                val animation = remember {
                    TargetBasedAnimation(
                        animationSpec = tween(DURATION_MILLIS),
                        typeConverter = Float.VectorConverter,
                        initialValue = 24f,
                        targetValue = 32f
                    )
                }
                var playTime by remember { mutableStateOf(0L) }
                LaunchedEffect(true) {
                    val startTime = withFrameNanos { it }
                    do {
                        playTime = withFrameNanos { it } - startTime
                        animationValue = animation.getValueFromNanos(playTime)
                        Log.d(TAG, "ContentScreen: $animationValue")
                    } while (!animation.isFinishedFromNanos(playTime))
                }
            }
            val textColor by animateColorAsState(
                targetValue = if (completion) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surfaceVariant,
                animationSpec = tween(DURATION_MILLIS)
            )

            Text(
                text = outcome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(textHorizontalPadding, 0.dp)
                    .horizontalScroll(reservoirScrollState),
                maxLines = 1,
                onTextLayout = reservoirTextLayout,
                style = RESERVOIR_TEXT_STYLE.copy(
                    color = textColor, fontSize = animationValue.sp
                )
            )
        }

        val entryModifier = if (isVerticalScreen) {
            Modifier
                .fillMaxWidth()
                .aspectRatio(5f / 6f)
        } else {
            Modifier
                .fillMaxWidth()
                .weight(1f)
        }

        var buttonHorizontalPadding = 14.dp
        var buttonVerticalPadding = 14.dp
        if (!isVerticalScreen) {
            buttonHorizontalPadding = 28.dp
            buttonVerticalPadding = 2.dp
        }
        Column(
            entryModifier
        ) {
            KEYBOARD_DICT.map {
                Row(
                    Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    it.map { text ->
                        val isEqualToken = text == EQUAL_TOKEN
                        TextButton(
                            onClick = { viewModel.onEntry(text) },
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(buttonHorizontalPadding, buttonVerticalPadding),
                            shape = if (isEqualToken) CircleShape else Shapes.Full,
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (isEqualToken) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                            )
                        ) {
                            Text(
                                text = text, color = when (text) {
                                    in DIGITAL_AREA -> MaterialTheme.colorScheme.onBackground
                                    in SYMBOL_AREA -> MaterialTheme.colorScheme.onSurface
                                    EQUAL_TOKEN -> MaterialTheme.colorScheme.surface
                                    else -> Color.Unspecified
                                }, style = NUMBER_TEXT_STYLE
                            )
                        }
                    }
                }
            }
        }

    }
}
