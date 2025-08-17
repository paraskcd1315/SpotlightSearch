package com.paraskcd.spotlightsearch.ui.pages.settings.colorpicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import com.paraskcd.spotlightsearch.enums.ColorOverrideKey
import com.paraskcd.spotlightsearch.enums.SearchResultDisplayMode
import com.paraskcd.spotlightsearch.types.SearchResult
import com.paraskcd.spotlightsearch.ui.components.SearchResultItem
import com.paraskcd.spotlightsearch.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ColorPickerPage(
    navController: NavController,
    keyRaw: String,
    repo: UserThemeRepository,
    vm: ThemeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val key = remember { ColorOverrideKey.valueOf(keyRaw) }
    val scope = rememberCoroutineScope()
    val scheme = MaterialTheme.colorScheme

    // Override actual (puede ser null)
    val overrideColor: Color? = when (key) {
        ColorOverrideKey.surface -> state.surface
        ColorOverrideKey.surfaceBright -> state.surfaceBright
        ColorOverrideKey.background -> state.background
        ColorOverrideKey.surfaceTint -> state.surfaceTint
        ColorOverrideKey.onSurface -> state.onSurface
        ColorOverrideKey.outline -> state.outline
    }

    // Color base (cuando no hay override) usando el esquema, nunca gris
    val baseColor: Color = when (key) {
        ColorOverrideKey.surface -> scheme.surface
        ColorOverrideKey.surfaceBright -> scheme.surfaceVariant
        ColorOverrideKey.background -> scheme.background
        ColorOverrideKey.surfaceTint -> scheme.surfaceTint
        ColorOverrideKey.onSurface -> scheme.onSurface
        ColorOverrideKey.outline -> scheme.outline
    }

    var userEdited by remember { mutableStateOf(false) }

    // Estado visible del picker (inicia con override si existe, si no con base)
    var color by remember { mutableStateOf(overrideColor ?: baseColor) }
    var hex by remember {
        mutableStateOf(TextFieldValue("#%06X".format((overrideColor ?: baseColor).toArgb() and 0xFFFFFF)))
    }

    // Si llega el override más tarde y el usuario no tocó, sincronizar
    LaunchedEffect(overrideColor) {
        if (!userEdited && overrideColor != null && overrideColor != color) {
            color = overrideColor
            hex = TextFieldValue("#%06X".format(overrideColor.toArgb() and 0xFFFFFF))
        }
    }

    fun updateHexFromColor(c: Color) {
        hex = TextFieldValue("#%06X".format(c.toArgb() and 0xFFFFFF))
    }

    fun syncHexToColor(txt: String) {
        val clean = txt.removePrefix("#")
        if (clean.length == 6 && clean.all { it in "0123456789ABCDEFabcdef" }) {
            val int = clean.toInt(16)
            val newColor = Color((0xFF000000 or int.toLong()).toInt())
            color = newColor
        }
    }

    fun save() {
        scope.launch {
            repo.merge(
                surface = if (key == ColorOverrideKey.surface) color.toArgb() else null,
                surfaceBright = if (key == ColorOverrideKey.surfaceBright) color.toArgb() else null,
                background = if (key == ColorOverrideKey.background) color.toArgb() else null,
                surfaceTint = if (key == ColorOverrideKey.surfaceTint) color.toArgb() else null,
                onSurface = if (key == ColorOverrideKey.onSurface) color.toArgb() else null,
                outline = if (key == ColorOverrideKey.outline) color.toArgb() else null,
            )
            navController.popBackStack()
        }
    }

    fun resetToDefault() {
        scope.launch {
            repo.clearSingle(key)
        }
    }

    val previewResult = remember(color, key) {
        SearchResult(
            title = "Preview",
            subtitle = key.title,
            icon = null,
            iconVector = null,
            displayMode = SearchResultDisplayMode.DEFAULT,
            isHeader = false,
            onClick = {}
        )
    }

    val previewContainerColor = when (key) {
        ColorOverrideKey.surface,
        ColorOverrideKey.surfaceBright -> color
        ColorOverrideKey.background -> color
        ColorOverrideKey.surfaceTint -> color.copy(alpha = 0.6f)
        ColorOverrideKey.onSurface -> scheme.surface
        ColorOverrideKey.outline -> scheme.surface
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Edit Color: ${key.title}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)

        Surface(
            shape = MaterialTheme.shapes.large,
            color = previewContainerColor,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                val contentColor = if (key == ColorOverrideKey.onSurface) color else scheme.onSurface
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    SearchResultItem(
                        result = previewResult.copy(
                            subtitle = "#%06X".format(color.toArgb() and 0xFFFFFF)
                        ),
                        onQueryChanged = {}
                    )
                }
            }
        }

        OutlinedTextField(
            value = hex,
            onValueChange = {
                userEdited = true
                hex = it
                syncHexToColor(it.text)
            },
            label = { Text("HEX") },
            singleLine = true
        )

        RGBSlider("R", (color.red * 255f)) {
            userEdited = true
            color = color.copy(red = it / 255f)
            updateHexFromColor(color)
        }
        RGBSlider("G", (color.green * 255f)) {
            userEdited = true
            color = color.copy(green = it / 255f)
            updateHexFromColor(color)
        }
        RGBSlider("B", (color.blue * 255f)) {
            userEdited = true
            color = color.copy(blue = it / 255f)
            updateHexFromColor(color)
        }

        Row {
            Button(
                onClick = { save() },
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
            ) {
                Text("Save")
            }
            Button(
                onClick = { resetToDefault() },
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
            ) {
                Text("Reset to default")
            }
        }

    }
}

@Composable
private fun RGBSlider(label: String, value: Float, onChange: (Float) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = MaterialTheme.colorScheme.onSurface)
            Text(value.roundToInt().toString(), color = MaterialTheme.colorScheme.onSurface)
        }
        Slider(
            value = value,
            onValueChange = { onChange(it.coerceIn(0f, 255f)) },
            valueRange = 0f..255f
        )
    }
}