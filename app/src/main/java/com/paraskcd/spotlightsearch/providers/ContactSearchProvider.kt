package com.paraskcd.spotlightsearch.providers

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import com.paraskcd.spotlightsearch.types.ActionButton
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.icons.SMS
import com.paraskcd.spotlightsearch.icons.WhatsApp

@Singleton
class ContactSearchProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val pm: PackageManager get() = context.packageManager

    private var cachedContacts: List<Triple<String, String, String?>>? = null

    private val contactObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(self: Boolean) {
            cachedContacts = null
        }
    }

    private var isObserving = false

    fun requiresPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
    }

    fun searchContacts(query: String): List<SearchResult> {
        if (requiresPermission()) return emptyList()

        if (cachedContacts == null) {
            cachedContacts = loadAllContacts()
        }

        val filtered = cachedContacts!!.filter { (name, _) ->
            name.contains(query, ignoreCase = true)
        }

        val waInstalled = resolveWhatsApp()

        return filtered.map { (name, number, photoUri) ->
            val drawable = photoUri?.let {
                try {
                    val uri = it.toUri()
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        Drawable.createFromStream(stream, null)
                    }
                } catch (_: Exception) {
                    null
                }
            }

            val actions = mutableListOf<ActionButton>()

            actions += ActionButton(label = "Call", iconVector = Icons.Default.Phone) {
                val intent = Intent(Intent.ACTION_DIAL, "tel:$number".toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            actions += ActionButton(label = "SMS", iconVector = SMS) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:$number")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
            if (waInstalled) {
                val sanitized = number.replace("[^\\d+]".toRegex(), "")
                actions += ActionButton(
                    label = "WhatsApp",
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://wa.me/$sanitized".toUri()
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                    iconVector = WhatsApp
                )
            }

            SearchResult(
                title = name,
                subtitle = number,
                icon = drawable,
                iconVector = if (drawable == null) Icons.Filled.Person else null,
                onClick = {
                    val contactUri = getContactUriByNumber(number)
                    contactUri?.let {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = it
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    }
                },
                onLongClick = {
                    val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
                    val clip = ClipData.newPlainText("Phone Number", number)
                    clipboard?.setPrimaryClip(clip)
                    Toast.makeText(context, "Phone number copied", Toast.LENGTH_SHORT).show()
                },
                actionButtons = actions,
                searchResultType = SearchResultType.CONTACT
            )
        }
    }

    private fun loadAllContacts(): List<Triple<String, String, String?>> {
        val results = mutableListOf<Triple<String, String, String?>>()

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )

        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoUriIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                val photoUri = it.getString(photoUriIndex)
                results.add(Triple(name, number, photoUri))
            }
        }

        return results
    }

    fun startObserving() {
        if (isObserving) return
        if (requiresPermission()) return
        context.contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            contactObserver
        )
        isObserving = true
    }

    fun stopObserving() {
        if (!isObserving) return
        try {
            context.contentResolver.unregisterContentObserver(contactObserver)
        } catch (_: Exception) { /* ignore */ }
        isObserving = false
    }

    private fun isInstalled(pkg: String): Boolean =
        try { pm.getApplicationInfo(pkg, 0); true } catch (_: Exception) { false }

    private fun resolveWhatsApp(): Boolean {
        val candidates = listOf("com.whatsapp", "com.whatsapp.w4b")
        return candidates.any { isInstalled(it) }
    }

    private fun getContactUriByNumber(number: String): Uri? {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        )
        val normalized = number.replace("[^\\d+]".toRegex(), "")
        val selection = "${ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER} = ?"
        val selectionArgs = arrayOf(normalized)

        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val contactId = it.getLong(0)
                val lookupKey = it.getString(1)
                return ContactsContract.Contacts.getLookupUri(contactId, lookupKey)
            }
        }
        return null
    }
}