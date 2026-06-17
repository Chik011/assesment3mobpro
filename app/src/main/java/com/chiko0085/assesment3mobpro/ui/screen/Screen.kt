package com.chiko0085.assesment3mobpro.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Gallery : Screen("gallery", "Galeri", Icons.Default.Collections)
    object Camera : Screen("camera", "Tambah Foto", Icons.Default.PhotoCamera)
    object Timer : Screen("timer", "Timer", Icons.Default.Timer)
}
