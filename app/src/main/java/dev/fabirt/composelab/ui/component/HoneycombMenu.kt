package dev.fabirt.composelab.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

val honeycombItems = listOf(
    HoneycombItem(Icons.Rounded.Home) { },
    HoneycombItem(Icons.Rounded.Notifications) { },
    HoneycombItem(Icons.Rounded.Email) { },
    HoneycombItem(Icons.Rounded.ShoppingCart) { },
    HoneycombItem(Icons.Rounded.Call) { },
    HoneycombItem(Icons.Rounded.Search) { },
)

@Composable
fun HoneycombMenu(
    items: List<HoneycombItem>
) {
    val currentState = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val angle by derivedStateOf {
        currentState.value * (90f + 45f)
    }
    val buttonSize = 80f

    Box(
        modifier = Modifier
    ) {

        for (item in items) {
            val index = items.indexOf(item)
            HexagonalButton(
                size = buttonSize.dp,
                onClick = item.onClick,
                color = Color.White,
                modifier = Modifier.absoluteOffset(
                    x = (currentState.value * (buttonSize + 4) * cos((60f * index - 90).toRadians())).dp,
                    y = (currentState.value * (buttonSize + 4) * sin((60f * index - 90).toRadians())).dp
                )
            ) {
                Icon(
                    imageVector = item.imageVector,
                    contentDescription = "",
                    tint = Color(0xFFD2D2D2),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        HexagonalButton(
            size = buttonSize.dp,
            onClick = {
                if (currentState.isRunning) return@HexagonalButton
                coroutineScope.launch {
                    val isCollapsed = currentState.value == 0f
                    val target = if (isCollapsed) 1f else 0f
                    currentState.animateTo(
                        targetValue = target,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = LinearEasing
                        )
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .rotate(angle)
            )
        }
    }
}

data class HoneycombItem(
    val imageVector: ImageVector,
    val onClick: () -> Unit
)

@Preview
@Composable
fun HoneycombMenuPreview() {
    Box(
        modifier = Modifier.size((80 * 3).dp),
        contentAlignment = Alignment.Center
    ) {
        HoneycombMenu(
            items = honeycombItems
        )
    }
}