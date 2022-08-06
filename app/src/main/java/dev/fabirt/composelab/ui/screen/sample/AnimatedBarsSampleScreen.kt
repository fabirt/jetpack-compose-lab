package dev.fabirt.composelab.ui.screen.sample

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.fabirt.composelab.ui.component.AnnotatedSystemUIRegion
import dev.fabirt.composelab.ui.component.BackButton
import dev.fabirt.composelab.util.random
import kotlinx.coroutines.delay

private const val ANIM_INTERVAL = 350L
private const val BAR_MAX_HEIGHT = 164f
private const val BAR_MIN_HEIGHT = 32f
private val BAR_COLOR = Color(0xFF1DB954)
private val BG_COLOR = Color(0xFF191414)

@Composable
fun AnimatedBarsSampleScreen(navController: NavController) {
    AnnotatedSystemUIRegion {
        AnimatedBarsScreenContent {
            navController.popBackStack()
        }
    }
}

@Composable
private fun AnimatedBarsScreenContent(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BG_COLOR)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        BackButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onBackClick
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.height(BAR_MAX_HEIGHT.dp)
        ) {
            AnimatedBar()
            AnimatedBar()
            AnimatedBar()
            AnimatedBar()
        }
    }
}

@Composable
private fun AnimatedBar(
    color: Color = BAR_COLOR,
    maxHeight: Float = BAR_MAX_HEIGHT,
    minHeight: Float = BAR_MIN_HEIGHT,
    initialHeight: Float = 0f,
) {
    var heightState by remember {
        mutableStateOf(initialHeight)
    }

    val height by animateFloatAsState(
        targetValue = heightState,
        animationSpec =  spring(
            stiffness = Spring.StiffnessVeryLow
        )
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(32.dp)
            .height(height.dp)
            .background(color, RoundedCornerShape(4.dp))
    )

    LaunchedEffect(heightState) {
        delay(ANIM_INTERVAL)
        heightState = random(from = minHeight, to = maxHeight)
    }
}

@Preview
@Composable
fun AnimatedBarsPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BG_COLOR),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.height(BAR_MAX_HEIGHT.dp)
        ) {
            AnimatedBar(initialHeight = 120f)
            AnimatedBar(initialHeight = 70f)
            AnimatedBar(initialHeight = 160f)
            AnimatedBar(initialHeight = 90f)
        }
    }
}