package com.chiko0085.assesment3mobpro.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chiko0085.assesment3mobpro.R
import com.chiko0085.assesment3mobpro.model.AlatGym
import com.chiko0085.assesment3mobpro.network.ApiStatus

@Composable
fun GalleryScreen(viewModel: MainViewModel) {
    val status by viewModel.status.collectAsState()
    val data by viewModel.data
    val user by viewModel.user.collectAsState(initial = null)

    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedAlat by remember { mutableStateOf<AlatGym?>(null) }

    // 🌟 TAMBAHAN: Otomatis memuat data pertama kali ketika user berhasil dideteksi login
    LaunchedEffect(user) {
        user?.let {
            viewModel.retrieveData(it.email)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (status) {
            ApiStatus.LOADING -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            ApiStatus.SUCCESS -> {
                if (data.isEmpty()) {
                    Text(
                        text = "Tidak ada data progres latihan.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(data) { alat ->
                            AlatGymItem(
                                alat = alat,
                                onDelete = {
                                    selectedAlat = alat
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
            ApiStatus.FAILED -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Gagal memuat data.")
                    IconButton(onClick = {
                        user?.let { viewModel.retrieveData(it.email) }
                    }) {
                        Icon(painter = painterResource(id = R.drawable.broken_img), contentDescription = "Retry")
                    }
                }
            }
        }

        if (showDeleteDialog && selectedAlat != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = "Hapus Progres") },
                text = { Text(text = "Apakah Anda yakin ingin menghapus progres '${selectedAlat!!.nama}'?") },
                confirmButton = {
                    TextButton(onClick = {
                        if (selectedAlat?.id != null && user != null) {
                            viewModel.deleteData(user!!.email, selectedAlat!!.id!!)
                        }
                        showDeleteDialog = false
                    }) {
                        Text(text = "Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(text = "Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun AlatGymItem(alat: AlatGym, onDelete: () -> Unit) {
    val context = LocalContext.current

    val imageModel = remember(alat.imageId) {
        if (alat.imageId.startsWith("http")) {
            alat.imageId
        } else if (alat.imageId.isEmpty()) {
            R.drawable.broken_img
        } else {
            try {
                android.util.Base64.decode(alat.imageId, android.util.Base64.DEFAULT)
            } catch (e: Exception) {
                R.drawable.broken_img
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageModel)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.broken_img),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = alat.nama, style = MaterialTheme.typography.titleMedium)
                Text(text = alat.deskripsi, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Gray
                )
            }
        }
    }
}