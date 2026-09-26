package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MessageSquare
import com.composables.icons.lucide.Phone
import com.paraskcd.spotlightsearch.designsystem.icons.WhatsApp
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpButton
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpButtonVariant
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.presentation.model.RowMenuItem
import com.paraskcd.spotlightsearch.sources.domain.model.actions.DialNumber
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenWhatsApp
import com.paraskcd.spotlightsearch.sources.domain.model.actions.SendSms
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit

@Composable
fun ContactActions(hit: ContactHit, onAction: (HitAction) -> Unit) {
    val buttons = buildList {
        add(RowMenuItem(stringResource(R.string.action_call), Lucide.Phone, DialNumber(hit.number)))
        add(RowMenuItem(stringResource(R.string.action_sms), Lucide.MessageSquare, SendSms(hit.number)))
        if (hit.hasWhatsApp) add(RowMenuItem(stringResource(R.string.action_whatsapp), WhatsApp, OpenWhatsApp(hit.number)))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = SpSpacing.s3),
        horizontalArrangement = Arrangement.spacedBy(SpSpacing.s2)
    ) {
        buttons.forEach { button ->
            SpButton(
                text = button.label,
                onClick = { onAction(button.action) },
                variant = SpButtonVariant.Secondary,
                icon = button.icon,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
