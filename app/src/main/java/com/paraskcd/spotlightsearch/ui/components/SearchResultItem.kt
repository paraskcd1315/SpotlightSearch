package com.paraskcd.spotlightsearch.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.paraskcd.spotlightsearch.types.SearchResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultItem(result: SearchResult) {
    val activity = LocalActivity.current

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
        exit = fadeOut()
    ) {
        if (result.isHeader) {
            Text(
                text = result.title,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
                fontWeight = FontWeight.Black
            )
        } else {
            var expanded by remember { mutableStateOf(false) }
            Surface(
                color = Color.White.copy(alpha = 0.0f),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .combinedClickable(
                        onClick = {
                            result.onClick()
                            activity?.finish()
                        },
                        onLongClick = { expanded = true },
                        enabled = result.onClick != {}
                    )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        result.icon?.let {
                            Image(
                                painter = rememberDrawablePainter(drawable = it),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .height(40.dp)
                                    .width(40.dp)
                                    .clip(CircleShape)
                            )
                        }
                        result.iconVector?.let {
                            Surface(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .height(40.dp)
                                    .width(40.dp)
                                    .clip(CircleShape),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Icon(
                                    imageVector = result.iconVector,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxSize()
                                )
                            }
                        }
                        Column {
                            Text(
                                result.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            result.subtitle?.let {
                                Text(it, color = Color.White, fontSize = 12.sp)
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
                                    onClick = action.onClick,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White.copy(alpha = 0.2f),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(action.label, maxLines = 1)
                                }
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
                                    color = Color.White
                                )
                            },
                            onClick = {
                                expanded = false
                                action.onClick()
                            },
                            leadingIcon = action.icon?.let {
                                {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = null,
                                        tint = Color.White
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