package dev.fabirt.composelab.util

import kotlin.random.Random

fun random(from: Float = 0f, to: Float = 1f): Float {
    return from + Random.nextFloat() * to
}