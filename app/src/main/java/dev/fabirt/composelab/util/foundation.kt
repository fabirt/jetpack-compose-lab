package dev.fabirt.composelab.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

typealias VoidCallback = () -> Unit

typealias ValueChange<T> = (T) -> Unit

@Composable
fun Float.toPx(): Float {
    return with(LocalDensity.current) { dp.toPx() }
}