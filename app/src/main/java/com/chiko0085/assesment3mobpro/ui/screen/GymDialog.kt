package com.chiko0085.assesment3mobpro.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chiko0085.assesment3mobpro.R

@Composable
fun GymDialog(
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String) -> Unit
) {
    var namaGerakan by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Progres Latihan")
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }
                OutlinedTextField(
                    value = namaGerakan,
                    onValueChange = { namaGerakan = it },
                    label = { Text(text = "Nama Gerakan") },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    label = { Text(text = "Catatan Latihan") },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = { onDismissRequest() }) {
                        Text(text = "Batal")
                    }
                    OutlinedButton(
                        onClick = { onConfirmation(namaGerakan, catatan) },
                        enabled = namaGerakan.isNotEmpty() && catatan.isNotEmpty(),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "Simpan")
                    }
                }
            }
        }
    }
}
