package com.indigo.mobileobservatory.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.indigo.mobileobservatory.ui.theme.ObservatoryTheme

/**
 * One primary destination of the app shell. The screen layer owns the mapping
 * between these entries and its own tab enum, so navigation stays testable and
 * the components stay free of screen-specific state.
 */
data class ObservatoryNavEntry(
    val key: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String = label,
)

/** Thumb-reachable bottom navigation for phones in portrait. */
@Composable
fun ObservatoryBottomBar(
    entries: List<ObservatoryNavEntry>,
    selectedKey: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column {
            DividerLine()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                entries.forEach { entry ->
                    BottomBarItem(
                        entry = entry,
                        selected = entry.key == selectedKey,
                        onClick = { onSelect(entry.key) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    entry: ObservatoryNavEntry,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    val indicatorColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primaryContainer
        else Color.Transparent,
        animationSpec = tween(durationMillis = 180),
        label = "nav-indicator"
    )
    val iconTint by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 180),
        label = "nav-icon-tint"
    )
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .observatoryClickable(shape, onClick = onClick)
            .semantics { contentDescription = entry.contentDescription },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 56.dp, height = 28.dp)
                .background(
                    color = indicatorColor,
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = entry.icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = entry.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/**
 * Side rail for landscape phones and tablets. [showLabels] is false on compact
 * landscape phones where vertical space is precious, true on tablets.
 */
@Composable
fun ObservatoryNavRail(
    entries: List<ObservatoryNavEntry>,
    selectedKey: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    showLabels: Boolean = true,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxHeight()
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(if (showLabels) 92.dp else 64.dp)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                entries.forEach { entry ->
                    RailItem(
                        entry = entry,
                        selected = entry.key == selectedKey,
                        showLabel = showLabels,
                        onClick = { onSelect(entry.key) }
                    )
                }
            }
            DividerLine(vertical = true)
        }
    }
}

@Composable
private fun RailItem(
    entry: ObservatoryNavEntry,
    selected: Boolean,
    showLabel: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    val indicatorColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primaryContainer
        else Color.Transparent,
        animationSpec = tween(durationMillis = 180),
        label = "rail-indicator"
    )
    val iconTint by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 180),
        label = "rail-icon-tint"
    )
    val indicatorWidth by animateDpAsState(
        targetValue = if (showLabel) 60.dp else 44.dp,
        animationSpec = tween(durationMillis = 180),
        label = "rail-indicator-width"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (showLabel) 64.dp else 52.dp)
            .padding(horizontal = 8.dp)
            .observatoryClickable(shape, onClick = onClick)
            .semantics { contentDescription = entry.contentDescription },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = indicatorWidth, height = 30.dp)
                .background(color = indicatorColor, shape = RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = entry.icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        if (showLabel) {
            Text(
                text = entry.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun DividerLine(vertical: Boolean = false) {
    val color = ObservatoryTheme.colors.hairline
    if (vertical) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(color)
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color)
        )
    }
}
