package com.chiko0085.assesment3mobpro.ui.screen

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiko0085.assesment3mobpro.model.AlatGym
import com.chiko0085.assesment3mobpro.model.User
import com.chiko0085.assesment3mobpro.network.ApiStatus
import com.chiko0085.assesment3mobpro.network.GymApi
import com.chiko0085.assesment3mobpro.network.UserDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainViewModel(private val dataStore: UserDataStore) : ViewModel() {

    var data = mutableStateOf(emptyList<AlatGym>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    val user = dataStore.userFlow

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val allData = GymApi.service.getAlatGym()
                val filteredData = allData.filter { it.userId == userId }
                data.value = filteredData
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = e.message
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, namaGerakan: String, catatan: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Perkecil ukuran gambar secara fisik agar muat di MockAPI (max 10-50KB)
                val optimizedBitmap = bitmap.adjustSize()
                val base64Image = optimizedBitmap.toBase64()
                
                val newAlat = AlatGym(
                    nama = namaGerakan,
                    deskripsi = catatan,
                    imageId = base64Image,
                    userId = userId
                )
                
                GymApi.service.postAlatGym(newAlat)
                retrieveData(userId)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error saving: ${e.message}")
                errorMessage.value = "Gagal menyimpan: Ukuran gambar terlalu besar untuk MockAPI."
            }
        }
    }

    fun deleteData(userId: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                GymApi.service.deleteAlatGym(id)
                retrieveData(userId)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error deleting: ${e.message}")
                errorMessage.value = e.message
            }
        }
    }

    // Helper untuk resize gambar ke resolusi rendah
    private fun Bitmap.adjustSize(): Bitmap {
        val targetSize = 300 // Resolusi rendah agar string Base64 pendek
        val ratio = width.toFloat() / height.toFloat()
        val targetWidth: Int
        val targetHeight: Int
        
        if (width > height) {
            targetWidth = targetSize
            targetHeight = (targetSize / ratio).toInt()
        } else {
            targetHeight = targetSize
            targetWidth = (targetSize * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
    }

    private fun Bitmap.toBase64(): String {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 40, stream) // Kualitas 40%
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun login(user: User) {
        viewModelScope.launch {
            dataStore.saveUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.clearUser()
        }
    }
}
