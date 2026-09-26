package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.ds.atoms.IconCircle
import com.paraskcd.spotlightsearch.search.infrastructure.icons.ContactPhotoLoader

@Composable
fun ContactPhoto(photoUri: String?, loader: ContactPhotoLoader, size: Dp) {
    val bitmap by produceState(photoUri?.let(loader::cached), photoUri) {
        value = photoUri?.let { loader.load(it) }
    }
    val photo = bitmap
    if (photo == null) {
        IconCircle(imageVector = Icons.Filled.Person, size = size)
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
}
