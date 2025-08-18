package com.paraskcd.spotlightsearch.ui.pages.settings.personalization

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import com.paraskcd.spotlightsearch.enums.ColorOverrideKey
import com.paraskcd.spotlightsearch.enums.ThemeMode
import com.paraskcd.spotlightsearch.icons.ChevronRight
import com.paraskcd.spotlightsearch.icons.Palette
import com.paraskcd.spotlightsearch.ui.components.BaseRowContainer
import com.paraskcd.spotlightsearch.icons.Blur
import com.paraskcd.spotlightsearch.ui.components.HeaderCard
import com.paraskcd.spotlightsearch.ui.components.GroupSurface
import com.paraskcd.spotlightsearch.ui.components.RowWithIcon
import com.paraskcd.spotlightsearch.ui.theme.ThemeUi
import com.paraskcd.spotlightsearch.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch

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

    val onToggle: (Boolean) -> Unit = { checked ->
        scope.launch { repo.setBlur(checked) }
    }

    LazyColumn {
        // Header
        item {
            HeaderCard("Appearance and Personalization", icon = Palette)
        }

        // Grupo principal: Theme / Blur / Icon Packs
        item {
            Text(
                "Appearance",
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        item {
            if (supportsBlur) {
                val rows = listOf("theme", "blur")
                GroupSurface(count = rows.size) { index, shape ->
                    when (rows[index]) {
                        "theme" ->
                            BaseRowContainer(shape = shape, onClick = { showThemeDialog = true }) {
                                RowWithIcon(
                                    icon = Palette,
                                    text = "Select theme"
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        state.mode.name.lowercase().replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Icon(ChevronRight, contentDescription = null)
                                }
                            }
                        "blur" ->
                            BaseRowContainer(shape = shape, onClick = { onToggle(!state.enableBlur) }) {
                                RowWithIcon(
                                    icon = Blur,
                                    text = "Enable Blur"
                                )
                                Switch(checked = state.enableBlur, onCheckedChange = onToggle)
                            }
                    }
                }
            } else {
                GroupSurface(count = 1) { index, shape ->
                    BaseRowContainer(shape, onClick = { showThemeDialog = true }) {
                        RowWithIcon(
                            icon = Palette,
                            text = "Select theme"
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                state.mode.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(ChevronRight, contentDescription = null)
                        }
                    }
                }
            }
        }

        // Título colores
        item {
            Text(
                "Personalization",
                modifier = Modifier
                    .padding(16.dp),
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
                        RowWithIcon(
                            icon = Palette,
                            text = "Reset colors"
                        )
                        Icon(ChevronRight, contentDescription = null)
                    }
                } else {
                    val key = keys[i - 1]
                    val color = currentColorForKey(state, key) ?: fallbackBaseColor(key)

                    BaseRowContainer(shape, onClick = { navController.navigate("settings_color_picker/${key.name}") }) {
                        RowWithIcon(
                            icon = Palette,
                            text = key.title
                        )
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