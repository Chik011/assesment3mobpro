package com.chiko0085.assesment3mobpro.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TimerScreen() {
    var workTimeInput by remember { mutableStateOf("45") }
    var restTimeInput by remember { mutableStateOf("15") }
    
    var timeLeft by remember { mutableIntStateOf(45) }
    var totalTime by remember { mutableIntStateOf(45) }
    var isRunning by remember { mutableStateOf(false) }
    var isWorkMode by remember { mutableStateOf(true) }

    // Efek Countdown
    LaunchedEffect(isRunning, timeLeft) {
        if (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else if (isRunning && timeLeft == 0) {
            // Tukar mode antara Work dan Rest
            isWorkMode = !isWorkMode
            val nextTime = if (isWorkMode) workTimeInput.toIntOrNull() ?: 45 else restTimeInput.toIntOrNull() ?: 15
            timeLeft = nextTime
            totalTime = nextTime
        }
    }

    val progress by animateFloatAsState(
        targetValue = if (totalTime > 0) timeLeft.toFloat() / totalTime else 0f,
        label = "TimerProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isWorkMode) "WAKTU LATIHAN" else "WAKTU ISTIRAHAT",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isWorkMode) MaterialTheme.colorScheme.primary else Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Lingkaran Timer
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(250.dp)) {
            Canvas(modifier = Modifier.size(250.dp)) {
                drawArc(
                    color = Color.LightGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = if (isWorkMode) Color(0xFF2196F3) else Color(0xFF4CAF50),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = timeLeft.toString(),
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Input Pengaturan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = workTimeInput,
                onValueChange = { workTimeInput = it },
                label = { Text("Latihan (s)") },
                modifier = Modifier.weight(1f),
                enabled = !isRunning
            )
            OutlinedTextField(
                value = restTimeInput,
                onValueChange = { restTimeInput = it },
                label = { Text("Istirahat (s)") },
                modifier = Modifier.weight(1f),
                enabled = !isRunning
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol Kontrol
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (!isRunning) {
                        val time = if (isWorkMode) workTimeInput.toIntOrNull() ?: 45 else restTimeInput.toIntOrNull() ?: 15
                        timeLeft = time
                        totalTime = time
                    }
                    isRunning = !isRunning
                },
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color.Red else Color.DarkGray
                )
            ) {
                Text(if (isRunning) "PAUSE" else "START")
            }

            Button(
                onClick = {
                    isRunning = false
                    isWorkMode = true
                    val time = workTimeInput.toIntOrNull() ?: 45
                    timeLeft = time
                    totalTime = time
                },
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            ) {
                Text("RESET")
            }
        }
    }
}
