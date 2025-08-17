package com.paraskcd.spotlightsearch.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.paraskcd.spotlightsearch.enums.SearchResultDisplayMode
import com.paraskcd.spotlightsearch.types.SearchResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultItem(result: SearchResult, onQueryChanged: (String) -> Unit) {
    val activity = LocalActivity.current
    val isCompact = result.displayMode == SearchResultDisplayMode.COMPACT
    var expanded by remember { mutableStateOf(false) }

    if (isCompact && !result.isHeader) {
        Column(
            modifier = Modifier
                .width(72.dp)
                .padding(8.dp)
                .combinedClickable(
                    onClick = {
                        result.onClick()
                        activity?.finish()
                    },
                    onLongClick = { expanded = true }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val icon = result.icon
            if (icon != null) {
                Image(
                    painter = rememberDrawablePainter(icon),
                    contentDescription = result.title,
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp)
                        .padding(6.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                )
            } else if (result.iconVector != null) {
                Surface(
                    modifier = Modifier
                        .height(54.dp)
                        .width(54.dp)
                        .padding(6.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f),
                ) {
                    Icon(
                        imageVector = result.iconVector,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    )
                }
            }
            Text(
                text = result.title,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 2,
                lineHeight = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        result.contextMenuActions?.takeIf { it.isNotEmpty() }?.let { actions ->
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                actions.forEach { action ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = action.title,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            expanded = false
                            action.onClick()
                            activity?.finish()
                        },
                        leadingIcon = action.icon?.let {
                            {
                                Icon(
                                    imageVector = it,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                    )
                }
            }
        }

        return
    }

    Column {
        if (result.isHeader) {
            Text(
                text = result.title,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(16.dp),
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleSmall
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = {
                            if (result.hasTextChangeFlag == true) {
                                onQueryChanged(result.subtitle.toString())
                                return@combinedClickable
                            }
                            result.onClick()
                            activity?.finish()
                        },
                        onLongClick = { expanded = true },
                        enabled = result.onClick != {}
                    ),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    result.icon?.let {
                        Image(
                            painter = rememberDrawablePainter(drawable = it),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(40.dp)
                                .width(40.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                        )
                    }
                    result.iconVector?.let {
                        Surface(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(40.dp)
                                .width(40.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f),
                        ) {
                            Icon(
                                imageVector = result.iconVector,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxSize()
                            )
                        }
                    }
                    Column {
                        Text(
                            result.title,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        result.subtitle?.let {
                            Text(it, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                result.actionButtons?.takeIf { it.isNotEmpty() }?.let { buttons ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        result.actionButtons.forEach { action ->
                            Button(
                                onClick = {
                                    action.onClick()
                                    activity?.finish()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f),
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(action.label, maxLines = 1)
                            }
                        }
                    }
                }
            }
            result.contextMenuActions?.takeIf { it.isNotEmpty() }?.let { actions ->
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    actions.forEach { action ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = action.title,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                expanded = false
                                action.onClick()
                                activity?.finish()
                            },
                            leadingIcon = action.icon?.let {
                                {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}