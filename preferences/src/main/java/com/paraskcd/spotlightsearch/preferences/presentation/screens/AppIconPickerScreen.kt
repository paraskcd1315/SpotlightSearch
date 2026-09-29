package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.RotateCcw
import com.composables.icons.lucide.Shapes
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpSearch
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.OptionSheet
import com.paraskcd.spotlightsearch.preferences.presentation.components.PackIconCell
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.components.ValueText
import com.paraskcd.spotlightsearch.preferences.presentation.utils.IconNameRanking
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.AppIconPickerViewModel

@Composable
fun AppIconPickerScreen(viewModel: AppIconPickerViewModel, onBack: () -> Unit) {
    val label by viewModel.label.collectAsState()
    val packs by viewModel.packs.collectAsState()
    val pack by viewModel.pack.collectAsState()
    val names by viewModel.names.collectAsState()
    val choice by viewModel.choice.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    var choosingPack by remember { mutableStateOf(false) }
    val rows = remember(names, label, query) {
        IconNameRanking.rank(names.orEmpty(), label, query).chunked(SettingsMetrics.PackIconColumns)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SpScreenScaffold(
            title = label,
            backDescription = stringResource(R.string.settings_back),
            onBack = onBack,
            attachment = {
                SpSearch(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = stringResource(R.string.app_icon_filter),
                    clearDescription = stringResource(R.string.blacklist_filter_clear)
                )
            }
        ) {
            val installed = packs
            if (installed == null) {
                item { SettingsSkeleton() }
                return@SpScreenScaffold
            }
            if (installed.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.app_icon_no_packs),
                        style = MaterialTheme.typography.bodyLarge,
                        color = SpTheme.colors.textTertiary,
                        modifier = Modifier.padding(horizontal = SpSpacing.s5)
                    )
                }
                return@SpScreenScaffold
            }
            item {
                SpGroupedList(count = if (choice != null) 2 else 1) { index ->
                    if (index == 0) {
                        SpSettingsRow(
                            label = stringResource(R.string.appearance_icon_pack),
                            icon = Lucide.Shapes,
                            onClick = { choosingPack = true },
                            trailing = { ValueText(pack?.label.orEmpty()) }
                        )
                    } else {
                        SpSettingsRow(
                            label = stringResource(R.string.app_icon_default),
                            icon = Lucide.RotateCcw,
                            onClick = {
                                viewModel.reset()
                                onBack()
                            }
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(SpMetrics.sectionGap)) }
            if (names == null) {
                item { SettingsSkeleton() }
                return@SpScreenScaffold
            }
            if (rows.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.settings_no_results),
                        style = MaterialTheme.typography.bodyLarge,
                        color = SpTheme.colors.textTertiary,
                        modifier = Modifier.padding(horizontal = SpSpacing.s5)
                    )
                }
            }
            val selectedPack = pack?.packageName.orEmpty()
            items(rows, key = { row -> "$selectedPack/${row.first()}" }) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SettingsMetrics.PagePadding, vertical = SettingsMetrics.PackIconRowSpacing / 2),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { drawable ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            PackIconCell(
                                iconPack = selectedPack,
                                drawable = drawable,
                                icons = viewModel.icons,
                                selected = choice?.iconPack == selectedPack && choice?.drawable == drawable,
                                onClick = {
                                    viewModel.choose(drawable)
                                    onBack()
                                }
                            )
                        }
                    }
                    repeat(SettingsMetrics.PackIconColumns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
        OptionSheet(
            visible = choosingPack,
            title = stringResource(R.string.appearance_icon_pack),
            options = packs.orEmpty(),
            selected = pack,
            label = { it?.label.orEmpty() },
            onSelect = {
                choosingPack = false
                if (it != null) viewModel.selectPack(it)
            },
            onDismiss = { choosingPack = false }
        )
    }
}
