package com.chiko0085.assesment3mobpro.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chiko0085.assesment3mobpro.R
import com.chiko0085.assesment3mobpro.model.User

@Composable
fun ProfilDialog(
    user: User,
    onDismissRequest: () -> Unit,
    onLogout: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_img),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = user.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(onClick = { onDismissRequest() }) {
                        Text(text = "Tutup")
                    }
                    OutlinedButton(
                        onClick = { onLogout() },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "Logout")
                    }
                }
            }
        }
    }
}
