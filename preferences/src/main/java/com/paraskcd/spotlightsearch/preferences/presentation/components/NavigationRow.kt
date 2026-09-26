package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.BaseRowContainer
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.RowWithIcon
import com.paraskcd.spotlightsearch.preferences.R

@Composable
fun NavigationRow(
    title: String,
    subtitle: String?,
    icon: ImageVector,
    trailingIcon: ImageVector,
    shape: RoundedCornerShape,
    onClick: () -> Unit
) {
    BaseRowContainer(shape = shape, onClick = onClick) {
        RowWithIcon(text = title, icon = icon, subtext = subtitle)
        Icon(imageVector = trailingIcon, contentDescription = stringResource(R.string.settings_go_to, title))
    }
}
