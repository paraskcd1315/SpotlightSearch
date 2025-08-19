package com.paraskcd.spotlightsearch.ui.pages.settings.colorpicker

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import com.paraskcd.spotlightsearch.enums.ColorOverrideKey
import com.paraskcd.spotlightsearch.enums.SearchResultDisplayMode
import com.paraskcd.spotlightsearch.icons.Palette
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
    val effectiveBlur = state.enableBlur
    var showConfirmReset by remember { mutableStateOf(false) }

    val overrideColor: Color? = when (key) {
        ColorOverrideKey.surfaceBright -> state.surfaceBright
        ColorOverrideKey.background -> state.background
        ColorOverrideKey.surfaceTint -> state.surfaceTint
        ColorOverrideKey.onSurface -> state.onSurface
        ColorOverrideKey.outline -> state.outline
    }

    val baseColor: Color = when (key) {
        ColorOverrideKey.surfaceBright -> scheme.surfaceBright
        ColorOverrideKey.background -> scheme.background
        ColorOverrideKey.surfaceTint -> scheme.surfaceTint
        ColorOverrideKey.onSurface -> scheme.onSurface
        ColorOverrideKey.outline -> scheme.outline
    }

    var userEdited by remember { mutableStateOf(false) }
    var color by remember { mutableStateOf(overrideColor ?: baseColor) }
    var hex by remember {
        mutableStateOf(TextFieldValue("#%06X".format((overrideColor ?: baseColor).toArgb() and 0xFFFFFF)))
    }

    // HSV (Hue 0..360, Saturation 0..1, Brightness 0..1)
    var hue by remember { mutableStateOf(0f) }
    var sat by remember { mutableStateOf(0f) }
    var bri by remember { mutableStateOf(0f) }

    fun updateHSVFromColor(c: Color) {
        val hsv = FloatArray(3)
        AndroidColor.colorToHSV(c.toArgb(), hsv)
        hue = hsv[0]
        sat = hsv[1]
        bri = hsv[2]
    }
    fun hsbToColor(h: Float, s: Float, b: Float): Color {
        return Color(AndroidColor.HSVToColor(floatArrayOf(h, s, b)))
    }
    fun applyHSV() {
        color = hsbToColor(hue, sat, bri)
        hex = TextFieldValue("#%06X".format(color.toArgb() and 0xFFFFFF))
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
            updateHSVFromColor(newColor)
        }
    }

    LaunchedEffect(overrideColor, baseColor) {
        if (!userEdited) {
            val target = overrideColor ?: baseColor
            color = target
            updateHexFromColor(target)
            updateHSVFromColor(target)
        }
    }


    val previewResult = remember(color, key) {
        SearchResult(
            title = "Preview",
            subtitle = key.title,
            icon = null,
            iconVector = Palette,
            displayMode = SearchResultDisplayMode.DEFAULT,
            isHeader = false,
            onClick = {}
        )
    }

    val liveScheme = remember(color, key, scheme) {
        when (key) {
            ColorOverrideKey.surfaceBright -> scheme.copy(surfaceBright = color)
            ColorOverrideKey.background -> scheme.copy(background = color)
            ColorOverrideKey.surfaceTint -> scheme.copy(surfaceTint = color)
            ColorOverrideKey.onSurface -> scheme.copy(onSurface = color)
            ColorOverrideKey.outline -> scheme.copy(outline = color)
        }
    }

    fun save() {
        scope.launch {
            repo.merge(
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
            userEdited = false
        }
    }

    fun revertUnsavedChanges() {
        userEdited = false
        val target = overrideColor ?: baseColor
        color = target
        updateHexFromColor(target)
        updateHSVFromColor(target)
    }

    val hueGradient = remember {
        val stops = listOf(0f, 60f, 120f, 180f, 240f, 300f, 360f)
        stops.map { hsbToColor(it, 1f, 1f) }
    }
    val satGradient = remember(hue, bri) {
        listOf(hsbToColor(hue, 0f, bri), hsbToColor(hue, 1f, bri))
    }
    val briGradient = remember(hue, sat) {
        listOf(hsbToColor(hue, sat, 0f), hsbToColor(hue, sat, 1f))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MaterialTheme(
            colorScheme = liveScheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes
        ) {
            Text(
                "Editing: ${key.title}, the searchbar is an interactive textfield - you can edit the hex and add your own",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (!effectiveBlur) MaterialTheme.colorScheme.background.copy(alpha = 0.9f) else MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = if (effectiveBlur) 0.65f else 1f),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        SearchResultItem(
                            result = previewResult.copy(
                                subtitle = "#%06X".format(color.toArgb() and 0xFFFFFF)
                            ),
                            onQueryChanged = {}
                        )
                    }
                    TextField(
                        value = hex,
                        onValueChange = {
                            userEdited = true
                            hex = it
                            syncHexToColor(it.text)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(100.dp)
                            ),
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceBright.copy(alpha = if (effectiveBlur) 0.65f else 1f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright.copy(alpha = if (effectiveBlur) 0.65f else 1f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 24.dp, end = 8.dp)
                            )
                        },
                    )
                }
            }
        }

        HSVSlider(
            label = "Hue",
            value = hue,
            valueRange = 0f..360f,
            gradient = Brush.horizontalGradient(hueGradient),
            valueLabel = { it.roundToInt().toString() }
        ) {
            userEdited = true
            hue = it
            applyHSV()
        }

        HSVSlider(
            label = "Saturation",
            value = sat,
            valueRange = 0f..1f,
            gradient = Brush.horizontalGradient(satGradient),
            valueLabel = { (it * 100).roundToInt().toString() + "%" }
        ) {
            userEdited = true
            sat = it
            applyHSV()
        }

        HSVSlider(
            label = "Brightness",
            value = bri,
            valueRange = 0f..1f,
            gradient = Brush.horizontalGradient(briGradient),
            valueLabel = { (it * 100).roundToInt().toString() + "%" }
        ) {
            userEdited = true
            bri = it
            applyHSV()
        }

        Row {
            Button(
                onClick = { save() },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) { Text("Save") }
            Button(
                onClick = {
                    if (overrideColor != null) {
                        // Hay un valor guardado en BD -> confirmar
                        showConfirmReset = true
                    } else {
                        // No hay override en BD: sólo revertir cambios locales sin diálogo
                        revertUnsavedChanges()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) { Text("Reset") }
        }

        if (showConfirmReset) {
            AlertDialog(
                onDismissRequest = { showConfirmReset = false },
                title = { Text("Reset color") },
                text = { Text("The color for ${key.title} will be changed back to default. This action can not be undone, are you sure?") },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmReset = false
                        resetToDefault()
                    }) { Text("Confirm") }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmReset = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
private fun HSVSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    gradient: Brush,
    valueLabel: (Float) -> String,
    onChange: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = MaterialTheme.colorScheme.onSurface)
            Text(valueLabel(value), color = MaterialTheme.colorScheme.onSurface)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Slider(
                value = value,
                onValueChange = { onChange(it.coerceIn(valueRange.start, valueRange.endInclusive)) },
                valueRange = valueRange,
                colors = SliderDefaults.colors(
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                    thumbColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}