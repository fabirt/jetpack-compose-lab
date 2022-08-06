package dev.fabirt.composelab.ui.screen.sample

import dev.fabirt.composelab.R
import dev.fabirt.composelab.ui.navigation.*

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
    SampleBuilder(
        "Animated Bars",
        Destination.Sample(SAMPLE_ANIMATED_BARS),
        R.drawable.sample_animated_bars,
        listOf("Challenge", "Animation"),
    ),
    SampleBuilder(
        "Air Drop Animation",
        Destination.Sample(SAMPLE_AIR_DROP_ANIMATION),
        R.drawable.sample_air_drop,
        listOf("Challenge", "Transition", "State holder", "Canvas"),
    ),
)
