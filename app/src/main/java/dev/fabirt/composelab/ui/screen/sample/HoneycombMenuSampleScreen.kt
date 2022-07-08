package dev.fabirt.composelab.ui.screen.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import dev.fabirt.composelab.ui.component.AnnotatedSystemUIRegion
import dev.fabirt.composelab.ui.component.BackButton
import dev.fabirt.composelab.ui.component.HoneycombMenu
import dev.fabirt.composelab.ui.component.honeycombItems

@Composable
fun HoneycombMenuSampleScreen(navController: NavController) {
    AnnotatedSystemUIRegion {
        HoneycombMenuScreenContent {
            navController.popBackStack()
        }
    }
}

@Composable
fun HoneycombMenuScreenContent(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        BackButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onBackClick
        )

        HoneycombMenu(
            items = honeycombItems
        )
    }
}

@Preview
@Composable
fun HoneycombMenuSampleScreenPreview() {
    HoneycombMenuScreenContent(
        onBackClick = {  }
    )
}