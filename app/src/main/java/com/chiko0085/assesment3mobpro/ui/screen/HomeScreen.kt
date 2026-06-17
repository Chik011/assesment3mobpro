package com.chiko0085.assesment3mobpro.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Info Aplikasi
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tentang Aplikasi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Smart Gym Tracker membantu Anda melacak progres latihan fisik dan memberikan informasi mengenai alat-alat gym yang tersedia.",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // BMI Calculator
        BmiCalculator()
    }
}

@Composable
fun BmiCalculator() {
    var berat by remember { mutableStateOf("") }
    var tinggi by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Float?>(null) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Kalkulator BMI",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = berat,
                onValueChange = { berat = it },
                label = { Text("Berat Badan (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tinggi,
                onValueChange = { tinggi = it },
                label = { Text("Tinggi Badan (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val w = berat.toFloatOrNull()
                    val h = tinggi.toFloatOrNull()?.div(100f) // convert cm to m
                    if (w != null && h != null && h > 0) {
                        bmiResult = w / (h * h)
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Hitung BMI")
            }

            bmiResult?.let { result ->
                val category = when {
                    result < 18.5 -> "Kurus"
                    result < 25.0 -> "Normal"
                    result < 30.0 -> "Gemuk"
                    else -> "Obesitas"
                }
                Text(
                    text = "Hasil BMI: %.1f".format(result),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = "Kategori: $category")
            }
        }
    }
}
