package dev.fabirt.composelab.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle

@Composable
fun FlashingText(
    modifier: Modifier = Modifier,
    state: FlashingTextState = rememberFlashingTextState(),
    style: TextStyle = LocalTextStyle.current,
    finishedStyle: TextStyle? = null
) {

    Crossfade(
        modifier = modifier,
        targetState = state.flashingState,
        animationSpec = tween(durationMillis = 400),
    ) { flashingState ->
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (flashingState) {
                FlashingTextState.FlashingState.Idle -> Text(
                    text = state.idleText,
                    style = style,
                )
                FlashingTextState.FlashingState.Flashing -> {
                    val transition = rememberInfiniteTransition()
                    val opacity by transition.animateFloat(
                        initialValue = 0.8f, targetValue = 0.1f, animationSpec = infiniteRepeatable(
                            animation = tween(800, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    Text(
                        text = state.flashingText,
                        style = style,
                        modifier = Modifier.alpha(opacity),
                    )
                }
                FlashingTextState.FlashingState.Busy -> Text(
                    text = state.busyText,
                    style = style.copy(
                        color = style.color.copy(alpha = 0.8f)
                    ),
                )
                FlashingTextState.FlashingState.Finished -> Text(
                    text = state.finishedText,
                    style = finishedStyle ?: style,
                )
            }
        }
    }
}

class FlashingTextState(
    val idleText: String,
    val flashingText: String,
    val busyText: String,
    val finishedText: String,
) {

    private var state by mutableStateOf<FlashingState>(FlashingState.Idle)

    val flashingState: FlashingState
        get() = state

    val isIdle: Boolean
        get() = state is FlashingState.Idle

    val isFlashing: Boolean
        get() = state is FlashingState.Flashing

    val isFinished: Boolean
        get() = state is FlashingState.Finished

    fun setIdle() {
        state = FlashingState.Idle
    }

    fun setFlashing() {
        state = FlashingState.Flashing
    }

    fun setBusy() {
        state = FlashingState.Busy
    }

    fun setFinished() {
        state = FlashingState.Finished
    }

    sealed class FlashingState {
        object Idle : FlashingState()
        object Flashing : FlashingState()
        object Busy : FlashingState()
        object Finished : FlashingState()
    }
}

@Composable
fun rememberFlashingTextState(
    idleText: String = "",
    flashingText: String = "",
    busyText: String = "",
    finishedText: String = "",
): FlashingTextState = remember(idleText, flashingText, busyText, finishedText) {
    FlashingTextState(idleText, flashingText, busyText, finishedText)
}
