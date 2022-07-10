package dev.fabirt.composelab.ui.screen.sample

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import dev.fabirt.composelab.ui.component.BackButton
import dev.fabirt.composelab.util.toPx
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun RopePhysicsSampleScreen(navController: NavController) {
    var offsetA by remember { mutableStateOf(Offset(-240f, 0f)) }
    var offsetB by remember { mutableStateOf(Offset(240f, 0f)) }

    RopePhysicsScreenContent(
        offsetA = offsetA,
        offsetB = offsetB,
        onNodeAChange = { offsetA += it },
        onNodeBChange = { offsetB += it },
        onBackClick = { navController.popBackStack() }
    )
}

@Composable
fun RopePhysicsScreenContent(
    offsetA: Offset,
    offsetB: Offset,
    onNodeAChange: (Offset) -> Unit,
    onNodeBChange: (Offset) -> Unit,
    onBackClick: () -> Unit
) {
    var focusedNode by remember {
        mutableStateOf<NodeId>(NodeId.None)
    }
    val nodeAScale by animateFloatAsState(
        targetValue = if (focusedNode == NodeId.A) 1.25f else 1f
    )
    val nodeBScale by animateFloatAsState(
        targetValue = if (focusedNode == NodeId.B) 1.25f else 1f
    )
    val threshold = 1500f
    var nodeAPosition by remember { mutableStateOf(Offset.Zero) }
    var nodeBPosition by remember { mutableStateOf(Offset.Zero) }
    val dx by derivedStateOf { nodeBPosition.x - nodeAPosition.x }
    val dy by derivedStateOf { nodeBPosition.y - nodeAPosition.y }
    val m by derivedStateOf {
        if (dx != 0f) dy / dx else 0f
    }
    val center by derivedStateOf {
        val ab = sqrt(dx.pow(2) + dy.pow(2))
        val x = nodeAPosition.x + dx / 2
        val b = -(m * nodeAPosition.x - nodeAPosition.y)
        var y = m * x + b
        var ac = ab / 2
        var bc = ac
        if (ab < threshold) {
            y += (threshold - ab) / 2

            ac = sqrt((nodeAPosition.x - x).pow(2) + (nodeAPosition.y - y).pow(2))
            bc = sqrt((nodeBPosition.x - x).pow(2) + (nodeBPosition.y - y).pow(2))
        }
        Log.i(
            "Rope/info", """Info
            p1: (${nodeAPosition.x}, ${nodeAPosition.y})
            p2: (${nodeBPosition.x}, ${nodeBPosition.y})
            dx: $dx
            dy: $dy
            m: $m
            b: $b
            ab: $ab
            ac: $ac
            bc: $bc
        """.trimIndent()
        )
        Offset(x, y)
    }

    val animatedCenter by animateOffsetAsState(
        targetValue = center,
        animationSpec = spring(
            dampingRatio = 0.3f,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        BackButton(
            tint = Color.Black,
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onBackClick
        )

        val strokeWidth = 8f.toPx()
        val nodeOffset = 24f.toPx()
        RopeNode(
            modifier = Modifier
                .zIndex(if (focusedNode == NodeId.A) 1f else 0f)
                .offset { IntOffset(offsetA.x.roundToInt(), offsetA.y.roundToInt()) }
                .onGloballyPositioned { coordinates ->
                    nodeAPosition = coordinates.positionInParent() + Offset(nodeOffset, nodeOffset)
                }
                .scale(nodeAScale)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { focusedNode = NodeId.A },
                        onDragEnd = { focusedNode = NodeId.None }
                    ) { change, dragAmount ->
                        change.consume()
                        onNodeAChange(dragAmount)
                    }
                }
        )

        RopeNode(
            modifier = Modifier
                .zIndex(if (focusedNode == NodeId.B) 1f else 0f)
                .offset { IntOffset(offsetB.x.roundToInt(), offsetB.y.roundToInt()) }
                .onGloballyPositioned { coordinates ->
                    nodeBPosition = coordinates.positionInParent() + Offset(nodeOffset, nodeOffset)
                }
                .scale(nodeBScale)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { focusedNode = NodeId.B },
                        onDragEnd = { focusedNode = NodeId.None }
                    ) { change, dragAmount ->
                        change.consume()
                        onNodeBChange(dragAmount)
                    }
                }
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            val useCubic = true
            val path = Path().apply {
                moveTo(nodeAPosition.x, nodeAPosition.y)
                if (useCubic) {
                    cubicTo(
                        nodeAPosition.x, nodeAPosition.y,
                        animatedCenter.x, animatedCenter.y,
                        nodeBPosition.x, nodeBPosition.y,
                    )
                } else {
                    lineTo(animatedCenter.x, animatedCenter.y)
                    lineTo(nodeBPosition.x, nodeBPosition.y)
                }
            }
            drawPath(
                path = path,
                color = Color.DarkGray,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
            )
        }

    }
}

@Composable
fun RopeNode(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
            )

        }
    }
}

@Preview(
    name = "Rope Node",
    backgroundColor = 16777215,
    showBackground = true,
    widthDp = 124,
    heightDp = 124
)
@Composable
fun RopeNodePreview() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        RopeNode()
    }
}

@Preview(
    name = "Rope Physics Screen",
)
@Composable
fun RopePhysicsScreenContentPreview() {
    RopePhysicsScreenContent(
        offsetA = Offset(-100f, 0f),
        offsetB = Offset(100f, 0f),
        onNodeAChange = { },
        onNodeBChange = { },
        onBackClick = { }
    )
}

sealed class NodeId {
    object A : NodeId()
    object B : NodeId()
    object None : NodeId()
}