package com.chiko0085.assesment3mobpro.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.chiko0085.assesment3mobpro.model.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Smart Gym Tracker",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Silakan login untuk melanjutkan",
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(onClick = {
            scope.launch {
                signIn(context, viewModel)
            }
        }) {
            Text(text = "Login with Google")
        }
    }
}

private suspend fun signIn(context: Context, viewModel: MainViewModel) {
    val credentialManager = CredentialManager.create(context)

    val googleIdOption = GetGoogleIdOption.Builder()
    .setFilterByAuthorizedAccounts(false)
    // ⚠️ MASUKKAN WEB CLIENT ID DI SINI (Bukan Android Client ID)
    .setServerClientId("489685199845-vun3svis9gh9e5ipbb62erafvsctinf6.apps.googleusercontent.com")
        .build()

    val request = GetCredentialRequest.Builder()
    .addCredentialOption(googleIdOption)
    .build()

    try {
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, viewModel)
    } catch (e: GetCredentialException) {
        Log.e("LoginScreen", "Error GetCredential: ${e.message}")
    }
}

private fun handleSignIn(result: GetCredentialResponse, viewModel: MainViewModel) {
    val credential = result.credential

    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            Log.d("LoginScreen", "Login Berhasil: ${googleIdTokenCredential.displayName}")

            val user = User(
                name = googleIdTokenCredential.displayName ?: "",
            email = googleIdTokenCredential.id,
            photoUrl = googleIdTokenCredential.profilePictureUri.toString()
            )
            viewModel.login(user)
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("LoginScreen", "Error Parsing: ${e.message}")
        }
    } else {
        Log.e("LoginScreen", "Tipe credential tidak dikenali: ${credential.type}")
    }
}