package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TvFocusBorder

/**
 * Modifier enabling smooth D-Pad navigation for Android TV & Google TV.
 * Highlights focused element with scaled elevation, border glow, and handles D-pad select.
 */
fun Modifier.tvFocusable(
    shape: Shape = RoundedCornerShape(12.dp),
    focusedScale: Float = 1.04f,
    borderWidth: Dp = 2.5.dp,
    borderColor: Color? = null,
    onEnterClick: (() -> Unit)? = null
): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isFocused) focusedScale else 1.0f, label = "tvFocusScale")
    val activeBorderColor = borderColor ?: MaterialTheme.colorScheme.primary

    this
        .onFocusChanged { isFocused = it.isFocused }
        .scale(scale)
        .then(
            if (isFocused) {
                Modifier.border(borderWidth, activeBorderColor, shape)
            } else {
                Modifier
            }
        )
        .onKeyEvent { keyEvent ->
            if (onEnterClick != null && (keyEvent.key == Key.DirectionCenter || keyEvent.key == Key.Enter)) {
                onEnterClick()
                true
            } else {
                false
            }
        }
        .focusable()
}
