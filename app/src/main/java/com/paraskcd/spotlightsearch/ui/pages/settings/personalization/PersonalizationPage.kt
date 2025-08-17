package com.paraskcd.spotlightsearch.ui.pages.settings.personalization

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import com.paraskcd.spotlightsearch.enums.ColorOverrideKey
import com.paraskcd.spotlightsearch.enums.ThemeMode
import com.paraskcd.spotlightsearch.ui.components.ChevronRight
import com.paraskcd.spotlightsearch.ui.theme.ThemeUi
import com.paraskcd.spotlightsearch.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun PersonalizationPage(
    navController: NavController,
    repo: UserThemeRepository,
    vm: ThemeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showResetColorsDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val supportsBlur = remember {
        (context as? Activity)?.windowManager?.isCrossWindowBlurEnabled == true
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item { HeaderCard() }

        // Grupo principal: Theme / Blur / Icon Packs
        item {
            if (supportsBlur) {
                val rows = listOf("theme", "blur")
                GroupSurface(count = rows.size) { index, shape ->
                    when (rows[index]) {
                        "theme" -> ThemeRow(
                            shape = shape,
                            current = state.mode,
                            onClick = { showThemeDialog = true }
                        )
                        "blur" -> BlurRow(
                            shape = shape,
                            checked = state.enableBlur,
                            onToggle = { scope.launch { repo.setBlur(it) } }
                        )
                    }
                }
            } else {
                GroupSurface(count = 1) { index, shape ->
                    ThemeRow(
                        shape = shape,
                        current = state.mode,
                        onClick = { showThemeDialog = true }
                    )
                }
            }
        }

        // Título colores
        item {
            Text(
                "Colors",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        // Grupo colores
        item {
            val keys = ColorOverrideKey.values().toList()
            GroupSurface(count = keys.size + 1) { i, shape ->
                if (i == 0) {
                    BaseRowContainer(
                        shape = shape,
                        onClick = { showResetColorsDialog = true }
                    ) {
                        Text("Reset colors", style = MaterialTheme.typography.titleMedium)
                        Icon(ChevronRight, contentDescription = null)
                    }
                } else {
                    val key = keys[i - 1]
                    val color = currentColorForKey(state, key) ?: fallbackBaseColor(key)
                    ColorRow(
                        key = key,
                        color = color,
                        shape = shape,
                        onClick = { navController.navigate("settings_color_picker/${key.name}") }
                    )
                }

            }
        }

        item { Spacer(Modifier.height(12.dp)) }
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            confirmButton = {},
            title = { Text("Theme Mode") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ThemeMode.values().forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (mode == state.mode)
                                        MaterialTheme.colorScheme.surfaceBright
                                    else
                                        Color.Transparent
                                )
                                .padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.mode == mode,
                                onClick = {
                                    showThemeDialog = false
                                    scope.launch { repo.setMode(mode) }
                                }
                            )
                            Text(
                                mode.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        )
    }

    if (showResetColorsDialog) {
        AlertDialog(
            onDismissRequest = { showResetColorsDialog = false },
            title = { Text("Reset colors") },
            text = { Text("¿Borrar todas las personalizaciones de color y volver a los dinámicos?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            repo.resetColors()
                            showResetColorsDialog = false
                        }
                    }
                ) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetColorsDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun HeaderCard() {
    var size by remember { mutableStateOf(IntSize.Zero) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        color = Color.Transparent,
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .onSizeChanged { size = it }
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.55f),
                            MaterialTheme.colorScheme.surface
                        ),
                        center = Offset(
                            (size.width / 2f),
                            (size.height / 2f)
                        ),
                        radius = (max(size.width, size.height)
                            .coerceAtLeast(1))   // evita 0
                            .toFloat() * 0.95f
                    )
                )
                .padding(56.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text("Appearance and Personalization", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.inverseOnSurface)
        }
    }
}

@Composable
private fun GroupSurface(
    count: Int,
    content: @Composable (index: Int, shape: RoundedCornerShape) -> Unit
) {
    Column {
        repeat(count) { i ->
            val shape = when {
                count == 1 -> RoundedCornerShape(24.dp)
                i == 0 -> RoundedCornerShape(
                    topStart = 24.dp, topEnd = 24.dp,
                    bottomStart = 8.dp, bottomEnd = 8.dp
                )
                i == count - 1 -> RoundedCornerShape(
                    topStart = 8.dp, topEnd = 8.dp,
                    bottomStart = 24.dp, bottomEnd = 24.dp
                )
                else -> RoundedCornerShape(8.dp)
            }
            content(i, shape)
        }
    }
}

@Composable
private fun BaseRowContainer(
    shape: RoundedCornerShape,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        onClick = onClick,
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceBright,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
private fun ThemeRow(
    shape: RoundedCornerShape,
    current: ThemeMode,
    onClick: () -> Unit
) {
    BaseRowContainer(shape, onClick) {
        Text("Select Theme", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                current.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(Modifier.width(8.dp))
            Icon(ChevronRight, contentDescription = null)
        }
    }
}

@Composable
private fun BlurRow(
    shape: RoundedCornerShape,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    BaseRowContainer(shape, onClick = { onToggle(!checked) }) {
        Text("Enable Blur", style = MaterialTheme.typography.titleMedium)
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

@Composable
private fun IconPacksRow(
    shape: RoundedCornerShape,
    hasSelection: Boolean,
    onClick: () -> Unit
) {
    BaseRowContainer(shape, onClick) {
        Text("Icon Packs", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasSelection) {
                Text(
                    "Active",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.width(8.dp))
            }
            Icon(ChevronRight, contentDescription = null)
        }
    }
}

@Composable
private fun ColorRow(
    key: ColorOverrideKey,
    color: Color,
    shape: RoundedCornerShape,
    onClick: () -> Unit
) {
    BaseRowContainer(shape, onClick) {
        Text(key.title, style = MaterialTheme.typography.titleMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier
                    .size(width = 54.dp, height = 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
            )
            Icon(ChevronRight, contentDescription = null)
        }
    }
}

private fun currentColorForKey(
    state: ThemeUi,
    key: ColorOverrideKey
): Color? = when (key) {
    ColorOverrideKey.surface -> state.surface
    ColorOverrideKey.surfaceBright -> state.surfaceBright
    ColorOverrideKey.background -> state.background
    ColorOverrideKey.surfaceTint -> state.surfaceTint
    ColorOverrideKey.onSurface -> state.onSurface
    ColorOverrideKey.outline -> state.outline
}

@Composable
private fun fallbackBaseColor(key: ColorOverrideKey): Color {
    val scheme = MaterialTheme.colorScheme
    return when (key) {
        ColorOverrideKey.surface -> scheme.surface
        ColorOverrideKey.surfaceBright -> scheme.surfaceVariant
        ColorOverrideKey.background -> scheme.background
        ColorOverrideKey.surfaceTint -> scheme.surfaceTint
        ColorOverrideKey.onSurface -> scheme.onSurface
        ColorOverrideKey.outline -> scheme.outline
    }
}