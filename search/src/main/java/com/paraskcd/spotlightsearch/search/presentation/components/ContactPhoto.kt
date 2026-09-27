package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpIconDisc
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.infrastructure.icons.ContactPhotoLoader

@Composable
fun ContactPhoto(photoUri: String?, loader: ContactPhotoLoader, size: Dp, work: Boolean = false) {
    val bitmap by produceState(photoUri?.let(loader::cached), photoUri) {
        value = photoUri?.let { loader.load(it) }
    }
    val photo = bitmap
    Box(modifier = Modifier.size(size)) {
        if (photo == null) {
            SpIconDisc(icon = Lucide.User, size = size)
        } else {
            Image(
                bitmap = photo.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        }
        if (work) WorkBadge(stringResource(R.string.work_contact_badge), Modifier.align(Alignment.BottomEnd))
    }
}
