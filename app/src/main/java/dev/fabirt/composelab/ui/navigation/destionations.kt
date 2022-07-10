package dev.fabirt.composelab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import dev.fabirt.composelab.ui.screen.sample.CompassSampleScreen
import dev.fabirt.composelab.ui.screen.sample.HoneycombMenuSampleScreen
import dev.fabirt.composelab.ui.screen.sample.RopePhysicsSampleScreen

val samples = mapOf<String, @Composable (NavController) -> Unit>(
    SAMPLE_HONEYCOMB_MENU to { navController -> HoneycombMenuSampleScreen(navController) },
    SAMPLE_COMPASS_VIEW to { navController -> CompassSampleScreen(navController) },
    SAMPLE_ROPE_PHYSICS to { navController -> RopePhysicsSampleScreen(navController) },
)

sealed class Destination(val route: String) {
    object Home : Destination(ROUTE_HOME)
    data class Sample(val id: String) : Destination(ROUTE_SAMPLE.replace("{id}", id))
}

fun NavController.pushDestination(destination: Destination) {
    navigate(destination.route)
}