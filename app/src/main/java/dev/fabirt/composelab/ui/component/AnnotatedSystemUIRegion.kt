package dev.fabirt.composelab.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AnnotatedSystemUIRegion(
    isAppearanceLight: Boolean = false,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val transparent = Color(0x01000000)

    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = transparent,
            darkIcons = isAppearanceLight
        )
        onDispose {
            systemUiController.setSystemBarsColor(
                color = transparent,
                darkIcons = !isAppearanceLight
            )
        }
    }

    content()
}