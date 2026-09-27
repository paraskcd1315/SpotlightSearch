package com.paraskcd.spotlightsearch.sources.infrastructure.launch

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.paraskcd.spotlightsearch.sources.R
import com.paraskcd.spotlightsearch.sources.domain.model.actions.CopyNumber
import com.paraskcd.spotlightsearch.sources.domain.model.actions.DialNumber
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.actions.LaunchApp
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenAppInfo
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenContact
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenContactsPermission
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenDeviceSetting
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenTranslator
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenWhatsApp
import com.paraskcd.spotlightsearch.sources.domain.model.actions.SearchWeb
import com.paraskcd.spotlightsearch.sources.domain.model.actions.SearchWith
import com.paraskcd.spotlightsearch.sources.domain.model.actions.SendSms
import com.paraskcd.spotlightsearch.sources.domain.repository.ActionRunner
import com.paraskcd.spotlightsearch.sources.infrastructure.devicesettings.DeviceSettingsCatalog
import com.paraskcd.spotlightsearch.sources.infrastructure.quicksearch.QuickSearchIntents
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IntentActionRunner @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ActionRunner {
    override fun run(action: HitAction) {
        if (action is CopyNumber) {
            copy(action.number)
            return
        }
        val intent = intentFor(action) ?: return
        try {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (e: ActivityNotFoundException) {
            fallbackFor(action)?.let { context.startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
                ?: Log.w(TAG, "No activity for $action", e)
        }
    }

    private fun intentFor(action: HitAction): Intent? = when (action) {
        is LaunchApp -> context.packageManager.getLaunchIntentForPackage(action.packageName)
        is OpenAppInfo -> appDetails(action.packageName)
        is DialNumber -> Intent(Intent.ACTION_DIAL, "tel:${action.number}".toUri())
        is SendSms -> Intent(Intent.ACTION_SENDTO, "smsto:${action.number}".toUri())
        is OpenWhatsApp -> Intent(Intent.ACTION_VIEW, "https://wa.me/${digits(action.number)}".toUri())
        is OpenContact -> contactUri(action.number)?.let { Intent(Intent.ACTION_VIEW, it) }
        is SearchWeb -> action.engine.urlTemplate
            ?.let { Intent(Intent.ACTION_VIEW, it.format(Uri.encode(action.query)).toUri()) }
            ?: Intent(Intent.ACTION_WEB_SEARCH).putExtra(SearchManager.QUERY, action.query)
        is SearchWith -> QuickSearchIntents.build(action.service, action.query)
        is OpenDeviceSetting -> Intent(DeviceSettingsCatalog.action(action.setting))
        is OpenTranslator -> googleTranslate(action)
        OpenContactsPermission -> appDetails(context.packageName)
        is CopyNumber -> null
    }

    private fun fallbackFor(action: HitAction): Intent? = when (action) {
        is OpenTranslator -> Intent(
            Intent.ACTION_VIEW,
            "https://translate.google.com/?sl=auto&tl=${action.targetLanguage}&text=${Uri.encode(action.text)}&op=translate".toUri()
        )
        else -> null
    }

    private fun appDetails(packageName: String) =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:$packageName".toUri())

    private fun googleTranslate(action: OpenTranslator) = Intent(Intent.ACTION_SEND).apply {
        component = ComponentName(GOOGLE_TRANSLATE_PACKAGE, GOOGLE_TRANSLATE_ACTIVITY)
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, action.text)
        putExtra("key_text_input", action.text)
        putExtra("key_text_output", "")
        putExtra("key_language_from", action.sourceLanguage ?: "auto")
        putExtra("key_language_to", action.targetLanguage)
        putExtra("key_suggest_translation", "")
        putExtra("key_from_floating_window", false)
    }

    private fun contactUri(number: String): Uri? {
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY),
            "${ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER} = ?",
            arrayOf(digits(number)),
            null
        ) ?: return null
        return cursor.use {
            if (it.moveToFirst()) ContactsContract.Contacts.getLookupUri(it.getLong(0), it.getString(1)) else null
        }
    }

    private fun copy(number: String) {
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java) ?: return
        clipboard.setPrimaryClip(
            ClipData.newPlainText(context.getString(R.string.sources_phone_number_clip_label), number)
        )
        Toast.makeText(context, R.string.sources_phone_number_copied, Toast.LENGTH_SHORT).show()
    }

    private fun digits(number: String) = number.replace(NON_DIGITS, "")

    private companion object {
        const val TAG = "IntentActionRunner"
        const val GOOGLE_TRANSLATE_PACKAGE = "com.google.android.apps.translate"
        const val GOOGLE_TRANSLATE_ACTIVITY = "com.google.android.apps.translate.TranslateActivity"
        val NON_DIGITS = Regex("[^\\d+]")
    }
}
