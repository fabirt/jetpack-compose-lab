package dev.fabirt.composelab.util

import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.TextInputService

/**
 * Clear focus and hide keyboard.
 */
fun clearFocus(
    focusManager: FocusManager,
    textInputService: TextInputService?
) {
    focusManager.clearFocus()
    textInputService?.hideSoftwareKeyboard()
}