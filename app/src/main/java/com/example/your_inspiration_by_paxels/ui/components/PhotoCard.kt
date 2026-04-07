package com.example.your_inspiration_by_paxels.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.your_inspiration_by_paxels.data.model.Photo
import kotlin.random.Random

@Composable
fun PhotoCard(
    photo: Photo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Generate a stable random height for the staggered effect
    val randomHeight = remember(photo.id) {
        Random(photo.id).nextInt(180, 320).dp
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            AsyncImage(
                model = photo.url,
                contentDescription = photo.alt,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(randomHeight)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
//            Column(modifier = Modifier.padding(12.dp)) {
//                Text(
//                    text = photo.photographer,
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
        }
    }
}
