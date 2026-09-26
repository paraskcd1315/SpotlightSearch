package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpButton
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpButtonVariant
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.presentation.components.ColorPreview
import com.paraskcd.spotlightsearch.preferences.presentation.components.ConfirmDialog
import com.paraskcd.spotlightsearch.preferences.presentation.components.HsvSlider
import com.paraskcd.spotlightsearch.preferences.presentation.model.Hsv
import com.paraskcd.spotlightsearch.preferences.presentation.utils.HsvColor
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.utils.applyTo
import com.paraskcd.spotlightsearch.preferences.presentation.utils.schemeColor
import com.paraskcd.spotlightsearch.preferences.presentation.utils.titleRes
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel
import kotlin.math.roundToInt

@Composable
fun ColorPickerScreen(key: ColorOverrideKey, viewModel: ThemeViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val scheme = MaterialTheme.colorScheme
    val saved = state.colors[key]
    val base = key.schemeColor(scheme)
    val title = stringResource(key.titleRes())

    var userEdited by remember { mutableStateOf(false) }
    var showConfirmReset by remember { mutableStateOf(false) }
    var color by remember { mutableStateOf(saved ?: base) }
    var hsv by remember { mutableStateOf(HsvColor.toHsv(saved ?: base)) }
    var hex by remember { mutableStateOf(TextFieldValue(HsvColor.hex(saved ?: base))) }

    fun show(target: Color) {
        color = target
        hsv = HsvColor.toHsv(target)
        hex = TextFieldValue(HsvColor.hex(target))
    }

    fun edit(next: Hsv) {
        userEdited = true
        hsv = next
        color = HsvColor.fromHsv(next.hue, next.saturation, next.brightness)
        hex = TextFieldValue(HsvColor.hex(color))
    }

    LaunchedEffect(saved, base) {
        if (!userEdited) show(saved ?: base)
    }

    val hueGradient = remember { HsvColor.HueStops.map { HsvColor.fromHsv(it, 1f, 1f) } }
    val saturationGradient = listOf(HsvColor.fromHsv(hsv.hue, 0f, hsv.brightness), HsvColor.fromHsv(hsv.hue, 1f, hsv.brightness))
    val brightnessGradient = listOf(HsvColor.fromHsv(hsv.hue, hsv.saturation, 0f), HsvColor.fromHsv(hsv.hue, hsv.saturation, 1f))

    Box(modifier = Modifier.fillMaxSize()) {
        SpScreenScaffold(title = title, backDescription = stringResource(R.string.settings_back), onBack = onBack) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = SpSpacing.s5),
                    verticalArrangement = Arrangement.spacedBy(SettingsMetrics.PageSpacing)
                ) {
                    Text(
                        text = stringResource(R.string.color_editing, title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = SpTheme.colors.textSecondary
                    )
                    MaterialTheme(colorScheme = key.applyTo(scheme, color), typography = MaterialTheme.typography) {
                        ColorPreview(hex = hex, blurEnabled = state.enableBlur) { value ->
                            userEdited = true
                            hex = value
                            HsvColor.parseHex(value.text)?.let {
                                color = it
                                hsv = HsvColor.toHsv(it)
                            }
                        }
                    }
                    HsvSlider(
                        label = stringResource(R.string.color_hue),
                        valueLabel = hsv.hue.roundToInt().toString(),
                        value = hsv.hue,
                        valueRange = HUE_RANGE,
                        gradient = Brush.horizontalGradient(hueGradient)
                    ) { edit(hsv.copy(hue = it)) }
                    HsvSlider(
                        label = stringResource(R.string.color_saturation),
                        valueLabel = stringResource(R.string.color_percent, (hsv.saturation * PERCENT).roundToInt()),
                        value = hsv.saturation,
                        valueRange = UNIT_RANGE,
                        gradient = Brush.horizontalGradient(saturationGradient)
                    ) { edit(hsv.copy(saturation = it)) }
                    HsvSlider(
                        label = stringResource(R.string.color_brightness),
                        valueLabel = stringResource(R.string.color_percent, (hsv.brightness * PERCENT).roundToInt()),
                        value = hsv.brightness,
                        valueRange = UNIT_RANGE,
                        gradient = Brush.horizontalGradient(brightnessGradient)
                    ) { edit(hsv.copy(brightness = it)) }
                    Row(horizontalArrangement = Arrangement.spacedBy(SpSpacing.s3)) {
                        SpButton(
                            text = stringResource(R.string.color_reset),
                            variant = SpButtonVariant.Secondary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (saved != null) {
                                    showConfirmReset = true
                                } else {
                                    userEdited = false
                                    show(base)
                                }
                            }
                        )
                        SpButton(
                            text = stringResource(R.string.color_save),
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.setColor(key, color.toArgb())
                                onBack()
                            }
                        )
                    }
                }
            }
        }
        ConfirmDialog(
            visible = showConfirmReset,
            title = stringResource(R.string.color_reset_title),
            message = stringResource(R.string.color_reset_message, title),
            confirmLabel = stringResource(R.string.color_reset_confirm),
            onConfirm = {
                showConfirmReset = false
                userEdited = false
                viewModel.clearColor(key)
            },
            onDismiss = { showConfirmReset = false }
        )
    }
}

private val HUE_RANGE = 0f..360f
private val UNIT_RANGE = 0f..1f
private const val PERCENT = 100
