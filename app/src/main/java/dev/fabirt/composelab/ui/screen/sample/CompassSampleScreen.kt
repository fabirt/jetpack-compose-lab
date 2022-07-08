package dev.fabirt.composelab.ui.screen.sample

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.fabirt.composelab.ui.component.AnnotatedSystemUIRegion
import dev.fabirt.composelab.ui.component.BackButton

@Composable
fun CompassSampleScreen(navController: NavController) {
    AnnotatedSystemUIRegion {
        CompassContent {
            navController.popBackStack()
        }
    }
}

@Composable
private fun CompassContent(
    initialTickCount: Float = -1f,
    initialNeedleScale: Float = 0f,
    initialNeedleRotation: Float = -30f,
    onBackClick: () -> Unit
) {
    val tickCount = remember { Animatable(initialTickCount) }
    val needleScale = remember { Animatable(initialNeedleScale) }
    val infiniteRotateTransition = rememberInfiniteTransition()
    val needleRotation by infiniteRotateTransition.animateFloat(
        initialValue = initialNeedleRotation,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        BackButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onBackClick
        )

        CompassView(
            tickCount = tickCount.value.toInt(),
            needleRotationDegrees = needleRotation,
            needleScale = needleScale.value
        )
    }

    LaunchedEffect(Unit) {
        tickCount.animateTo(
            targetValue = 60f,
            animationSpec = tween(
                durationMillis = 1600,
                delayMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        needleScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1600,
                easing = FastOutSlowInEasing
            )
        )
    }
}

@Composable
private fun CompassView(
    tickCount: Int = 60,
    needleRotationDegrees: Float = 0f,
    needleScale: Float = 1f,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .padding(24.dp)
            .shadow(8.dp, CircleShape)
            .border(12.dp, Color.White, CircleShape)
            .background(Color(0xFF1565C0), CircleShape)
            .padding(20.dp)
            .drawBehind {
                val radius = size.width / 2f
                val angle = 6f
                val largeTickLength = 50f
                val smallTickLength = 20f
                translate(0f, 0f) {
                    for (i in 0..tickCount) {
                        val tickLength = if (i.isEven()) largeTickLength else smallTickLength
                        rotate(angle * i, pivot = center) {
                            drawLine(
                                color = Color.White,
                                start = Offset(radius, 0f),
                                end = Offset(radius, tickLength),
                                strokeWidth = 8f,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            },
    ) {
        CompassNeedle(
            modifier = Modifier
                .rotate(needleRotationDegrees)
                .scale(needleScale)
        )
    }
}

@Composable
private fun CompassNeedle(
    modifier: Modifier = Modifier
) {
    val pathWidth = 80f
    Canvas(modifier = modifier.fillMaxSize()) {
        val p1 = Path().apply {
            moveTo(center.x, 0f)
            lineTo(center.x, center.y)
            lineTo(center.x - pathWidth, center.y)
            close()
        }
        val p2 = Path().apply {
            moveTo(center.x, 0f)
            lineTo(center.x, center.y)
            lineTo(center.x + pathWidth, center.y)
            close()
        }
        val p3 = Path().apply {
            moveTo(center.x, center.y)
            lineTo(center.x - pathWidth, center.y)
            lineTo(center.x, size.height)
            close()
        }
        val p4 = Path().apply {
            moveTo(center.x, center.y)
            lineTo(center.x + pathWidth, center.y)
            lineTo(center.x, size.height)
            close()
        }
        val pShadow = Path().apply {
            moveTo(center.x - 24f, 0f)
            lineTo(center.x - 24f + pathWidth, center.y)
            lineTo(center.x - 24f, size.height)
            lineTo(center.x - 24f - pathWidth, center.y)
            close()
        }

        drawPath(
            path = pShadow,
            brush = Brush.horizontalGradient(
                colorStops = arrayOf(
                    0.3f to Color.Transparent,
                    1.0f to Color.Black
                ),
            )
        )
        drawPath(
            path = p1,
            color = Color(0xFFFF5F52),
        )
        drawPath(
            path = p2,
            color = Color(0xFFE53935),
        )
        drawPath(
            path = p3,
            color = Color(0xFFF0F0F0),
        )
        drawPath(
            path = p4,
            color = Color.LightGray,
        )
    }
}

fun Int.isEven(): Boolean = this % 2 == 0

@Preview
@Composable
fun CompassSampleScreenPreview() {
    CompassContent(
        initialTickCount = 60f,
        initialNeedleScale = 1f
    ) { }
}