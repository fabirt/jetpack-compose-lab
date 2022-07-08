package dev.fabirt.composelab.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.fabirt.composelab.ui.theme.ComposeLabTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HexagonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFF9900),
    size: Dp = 70.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(HexagonShape)
            .background(color)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

val HexagonShape = GenericShape { size, _ ->
    val radius = size.width / 2f
    val angle = 60f
    var xRef = 0f
    var yRef = size.width / 2f
    var sideCount = 0
    moveTo(xRef, yRef)

    while (sideCount < 5) {
        val theta = (angle - (angle * sideCount)).toRadians()
        val x = radius * cos(theta) + xRef
        val y = radius * sin(theta) + yRef

        lineTo(x, y)

        xRef = x
        yRef = y
        sideCount++
    }
    close()
}

fun Float.toRadians(): Float = (this * PI / 180f).toFloat()

@Composable
@Preview
fun HexagonalButtonPreview() {
    ComposeLabTheme {
        HexagonalButton(
            onClick = {  }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}