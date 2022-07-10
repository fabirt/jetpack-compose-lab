package dev.fabirt.composelab.ui.screen.sample

import dev.fabirt.composelab.R
import dev.fabirt.composelab.ui.navigation.Destination
import dev.fabirt.composelab.ui.navigation.SAMPLE_COMPASS_VIEW
import dev.fabirt.composelab.ui.navigation.SAMPLE_HONEYCOMB_MENU
import dev.fabirt.composelab.ui.navigation.SAMPLE_ROPE_PHYSICS

data class SampleBuilder(
    val title: String,
    val destination: Destination,
    val thumbnailResId: Int?,
    val tags: List<String>?
)

val samplesMenu = listOf(
    SampleBuilder(
        "Honeycomb menu",
        Destination.Sample(SAMPLE_HONEYCOMB_MENU),
        R.drawable.honeycomb_menu,
        listOf("Animatable", "Transition", "Shapes"),
    ),
    SampleBuilder(
        "Compass view",
        Destination.Sample(SAMPLE_COMPASS_VIEW),
        R.drawable.compass_sample,
        listOf("Canvas", "Infinite", "Easing", "Transition"),
    ),
    SampleBuilder(
        "Rope physics",
        Destination.Sample(SAMPLE_ROPE_PHYSICS),
        R.drawable.rope_sample,
        listOf("Gestures", "Drag", "Curves", "Transition"),
    ),
)
