package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.designsystem.icons.SMS
import com.paraskcd.spotlightsearch.designsystem.icons.WhatsApp
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.presentation.model.RowMenuItem
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.sources.domain.model.actions.DialNumber
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenWhatsApp
import com.paraskcd.spotlightsearch.sources.domain.model.actions.SendSms
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit

@Composable
fun ContactActions(hit: ContactHit, onAction: (HitAction) -> Unit) {
    val buttons = buildList {
        add(RowMenuItem(stringResource(R.string.action_call), Icons.Filled.Phone, DialNumber(hit.number)))
        add(RowMenuItem(stringResource(R.string.action_sms), SMS, SendSms(hit.number)))
        if (hit.hasWhatsApp) add(RowMenuItem(stringResource(R.string.action_whatsapp), WhatsApp, OpenWhatsApp(hit.number)))
    }
    Spacer(modifier = Modifier.height(SearchMetrics.ActionSpacing))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        buttons.forEach { button ->
            Button(
                onClick = { onAction(button.action) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = SearchMetrics.ActionButtonSpacing),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = DsMetrics.TintAlpha),
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SearchMetrics.ActionSpacing, Alignment.CenterHorizontally)
                ) {
                    button.icon?.let { Icon(it, button.label, modifier = Modifier.size(SearchMetrics.ActionIconSize)) }
                    Text(button.label, maxLines = 1)
                }
            }
        }
    }
}
