package com.chiko0085.assesment3mobpro.ui.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.chiko0085.assesment3mobpro.network.UserDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(dataStore))
    val user by viewModel.user.collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var showDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            bitmap = result.uriContent?.toBitmap(context)
            showDialog = true
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            viewModel.retrieveData(user!!.email)
        }
    }

    if (user == null) {
        LoginScreen(viewModel = viewModel)
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = currentScreen.title) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    actions = {
                        IconButton(onClick = { showProfileDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            },
            bottomBar = {
                val items = listOf(Screen.Home, Screen.Gallery, Screen.Timer, Screen.Camera)
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentScreen == screen,
                            onClick = {
                                if (screen == Screen.Camera) {
                                    val options = CropImageContractOptions(
                                        null, CropImageOptions(
                                            imageSourceIncludeGallery = false,
                                            imageSourceIncludeCamera = true,
                                            fixAspectRatio = true,
                                            aspectRatioX = 1,
                                            aspectRatioY = 1
                                        )
                                    )
                                    launcher.launch(options)
                                } else {
                                    currentScreen = screen
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentScreen) {
                    Screen.Home -> HomeScreen()
                    Screen.Gallery -> GalleryScreen(viewModel = viewModel)
                    Screen.Timer -> TimerScreen()
                    else -> {} // Camera handled by launcher
                }
            }
        }

        if (showDialog && bitmap != null) {
            GymDialog(
                bitmap = bitmap,
                onDismissRequest = { showDialog = false },
                onConfirmation = { nama, catatan ->
                    viewModel.saveData(user!!.email, nama, catatan, bitmap!!)
                    showDialog = false
                }
            )
        }

        if (showProfileDialog) {
            ProfilDialog(
                user = user!!,
                onDismissRequest = { showProfileDialog = false },
                onLogout = {
                    scope.launch {
                        signOut(context, viewModel)
                        showProfileDialog = false
                    }
                }
            )
        }
    }
}

private fun android.net.Uri.toBitmap(context: Context): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }
}

private suspend fun signOut(context: Context, viewModel: MainViewModel) {
    val credentialManager = CredentialManager.create(context)
    try {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        viewModel.logout()
    } catch (e: ClearCredentialException) {
        Log.e("MainScreen", "Error: ${e.message}")
    }
}
