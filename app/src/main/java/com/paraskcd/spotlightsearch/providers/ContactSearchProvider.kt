package com.paraskcd.spotlightsearch.providers

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
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
    @ApplicationContext private val context: Context
) {
    // In-memory cache of all contacts as (name, number, photoUri?) triples
    private var cachedContacts: List<Triple<String, String, String?>>? = null

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
                    val uri = Uri.parse(it)
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        Drawable.createFromStream(stream, null)
                    }
                } catch (e: Exception) {
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
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
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
}