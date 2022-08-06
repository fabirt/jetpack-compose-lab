package dev.fabirt.composelab.ui.screen.sample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.fabirt.composelab.ui.component.AnnotatedSystemUIRegion
import dev.fabirt.composelab.ui.component.BackButton
import dev.fabirt.composelab.ui.component.FlashingText
import dev.fabirt.composelab.ui.component.rememberFlashingTextState
import dev.fabirt.composelab.util.ValueProvider
import dev.fabirt.composelab.util.VoidCallback
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AirDropSampleScreen(navController: NavController) {
    AnnotatedSystemUIRegion {
        AirDropScreenContent {
            navController.popBackStack()
        }
    }
}

@Composable
private fun AirDropScreenContent(
    onBackClick: VoidCallback
) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember {
        mutableStateOf<Job?>(null)
    }

    val colors = MaterialTheme.colors.copy(
        background = Color(0xFF222222),
        onBackground = Color(0xFFFFFFFF),
        surface = Color(0x8AFFFFFF),
        primary = Color(0xFF147EFB)
    )

    val flashingTextState = rememberFlashingTextState(
        "from Amos",
        "Waiting...",
        "Sending...",
        "Sent"
    )

    val progressAngle = remember { Animatable(0f) }

    var borderVisible by remember { mutableStateOf(true) }

    MaterialTheme(
        colors = colors
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            BackButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = onBackClick,
                tint = MaterialTheme.colors.onBackground
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures {
                        if (job?.isActive == true) return@detectTapGestures

                        job = coroutineScope.launch {
                            borderVisible = true
                            progressAngle.snapTo(0f)
                            flashingTextState.setFlashing()
                            delay(5000)
                            flashingTextState.setBusy()
                            delay(300)
                            progressAngle.animateTo(
                                targetValue = 360f,
                                animationSpec = tween(
                                    durationMillis = 3000
                                )
                            )
                            delay(300)
                            flashingTextState.setFinished()
                            delay(300)
                            borderVisible = false
                        }
                    }
                }
            ) {
                ProgressCircle(
                    borderVisibleProvider = { borderVisible },
                    sweepAngleProvider = { progressAngle.value }
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colors.surface,
                        modifier = Modifier.size(240.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "iPhone", style = MaterialTheme.typography.h6.copy(
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
                FlashingText(
                    state = flashingTextState,
                    style = MaterialTheme.typography.h6.copy(
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center
                    ),
                    finishedStyle = MaterialTheme.typography.h6.copy(
                        color = MaterialTheme.colors.primary,
                        textAlign = TextAlign.Center
                    )
                )
            }

        }
    }
}

@Composable
private fun ProgressCircle(
    borderVisibleProvider: ValueProvider<Boolean>,
    sweepAngleProvider: ValueProvider<Float>,
    content: @Composable VoidCallback
) {
    val colors = MaterialTheme.colors
    val sweepAngle = sweepAngleProvider()
    val animTween = tween<Float>(800)

    Box {
        AnimatedVisibility(
            visible = borderVisibleProvider(),
            modifier = Modifier.align(Alignment.Center),
            enter = fadeIn(animTween),
            exit = fadeOut(animTween),
        ) {
            Canvas(
                modifier = Modifier.align(Alignment.Center)
            ) {
                val radius = 110.dp.toPx()
                drawCircle(
                    color = colors.surface,
                    style = Stroke(8.dp.toPx()),
                    radius = radius
                )
                val arcSize = Size(radius * 2, radius * 2)
                drawArc(
                    color = colors.primary,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    size = arcSize,
                    style = Stroke(8.dp.toPx()),
                    topLeft = center - Offset(arcSize.width / 2, arcSize.height / 2)
                )
            }
        }
        content()
    }
}

@Preview
@Composable
fun AirDropSamplePreview() {
    AirDropScreenContent {

    }
}
