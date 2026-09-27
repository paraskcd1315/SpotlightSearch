package com.paraskcd.spotlightsearch.sources.infrastructure.contacts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactsSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val whatsAppPackages = listOf("com.whatsapp", "com.whatsapp.w4b")

    fun hasPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED

    fun load(): List<PhoneContact> {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
        )
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null
        ) ?: return emptyList()

        return cursor.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)
            buildList {
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex) ?: continue
                    val number = it.getString(numberIndex) ?: continue
                    add(PhoneContact(name, number, it.getString(photoIndex)))
                }
            }
        }
    }

    fun searchWork(query: String): List<PhoneContact> {
        if (query.isEmpty()) return emptyList()
        val uri = ContactsContract.CommonDataKinds.Phone.ENTERPRISE_CONTENT_FILTER_URI.buildUpon()
            .appendPath(query)
            .appendQueryParameter(ContactsContract.DIRECTORY_PARAM_KEY, ContactsContract.Directory.ENTERPRISE_DEFAULT.toString())
            .build()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
        )
        val cursor = runCatching { context.contentResolver.query(uri, projection, null, null, null) }
            .onFailure { Log.w(TAG, "Work contacts unavailable", it) }
            .getOrNull() ?: return emptyList()
        return cursor.use {
            buildList {
                while (it.moveToNext()) {
                    val id = it.getLong(0)
                    val lookupKey = it.getString(1) ?: continue
                    val name = it.getString(2) ?: continue
                    val number = it.getString(3) ?: continue
                    if (!ContactsContract.Contacts.isEnterpriseContactId(id)) continue
                    val lookupUri = ContactsContract.Contacts.getLookupUri(id, lookupKey).toString()
                    add(PhoneContact(name, number, it.getString(4), lookupUri))
                }
            }
        }
    }

    fun hasWhatsApp(): Boolean = whatsAppPackages.any { packageName ->
        runCatching { context.packageManager.getApplicationInfo(packageName, 0) }.isSuccess
    }

    fun observeChanges(onChange: () -> Unit) {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) = onChange()
        }
        context.contentResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, observer)
    }

    private companion object {
        const val TAG = "ContactsSource"
    }
}
