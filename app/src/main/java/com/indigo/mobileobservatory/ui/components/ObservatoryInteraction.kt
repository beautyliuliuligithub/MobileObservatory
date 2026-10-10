package com.indigo.mobileobservatory.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Press feedback shared by the cockpit's own controls.
 *
 * The ripple is clipped to the control's shape (capsules previously showed a
 * rectangular ripple around themselves) and the surface dips slightly while the
 * finger is down. Both are deliberately short and run on the render layer only,
 * so the preview-heavy camera screen stays smooth.
 */
@Composable
fun Modifier.observatoryClickable(
    shape: Shape,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val indication = LocalIndication.current
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "press-scale"
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clip(shape)
        .clickable(
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            onClick = onClick
        )
}

/**
 * One-shot fade-and-lift used when a page's cards first appear. Deliberately
 * limited to the first composition and a handful of items: the animation
 * releases the frame clock when it finishes instead of running forever.
 */
@Composable
fun StaggeredAppear(
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay((index.coerceIn(0, 5) * 35).toLong())
        visible = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 190),
        label = "appear-alpha"
    )
    val lift by animateDpAsState(
        targetValue = if (visible) 0.dp else 6.dp,
        animationSpec = tween(durationMillis = 190),
        label = "appear-lift"
    )
    Box(
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
            translationY = lift.toPx()
        }
    ) {
        content()
    }
}
