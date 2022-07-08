package dev.fabirt.composelab.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dev.fabirt.composelab.R
import dev.fabirt.composelab.ui.navigation.ROUTE_HOME
import dev.fabirt.composelab.ui.navigation.ROUTE_SAMPLE
import dev.fabirt.composelab.ui.navigation.samples
import dev.fabirt.composelab.ui.screen.HomeScreen
import dev.fabirt.composelab.ui.theme.ComposeLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_App)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { App() }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {
    val navController = rememberAnimatedNavController()
    ComposeLabTheme(
        darkTheme = false
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = ROUTE_HOME,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                },
            ) {
                composable(ROUTE_HOME) {
                    HomeScreen(navController)
                }
                composable(ROUTE_SAMPLE) { backStackEntry ->
                    val sampleId = backStackEntry.arguments?.getString("id") ?: ""
                    samples[sampleId]?.invoke(navController)
                }
            }
        }
    }
}
