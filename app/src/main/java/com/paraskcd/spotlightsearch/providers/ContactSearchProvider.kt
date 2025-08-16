package com.paraskcd.spotlightsearch.providers

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.paraskcd.spotlightsearch.types.ActionButton
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.paraskcd.spotlightsearch.enums.SearchResultType

@Singleton
class ContactSearchProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
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
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return emptyList()
        }

        if (cachedContacts == null) {
            cachedContacts = loadAllContacts()
        }

        val filtered = cachedContacts!!.filter { (name, _) ->
            name.contains(query, ignoreCase = true)
        }

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

            SearchResult(
                title = name,
                subtitle = number,
                icon = drawable,
                iconVector = if (drawable == null) Icons.Filled.Person else null,
                onClick = {},
                actionButtons = listOf(
                    ActionButton("Call") {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:$number".toUri())
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                    ActionButton("SMS") {
                        val intent = Intent(Intent.ACTION_VIEW, "sms:$number".toUri())
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                    ActionButton("WhatsApp") {
                        val intent = Intent(Intent.ACTION_VIEW,
                            "https://wa.me/${number.replace("[^\\d+]".toRegex(), "")}".toUri())
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                ),
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
}
