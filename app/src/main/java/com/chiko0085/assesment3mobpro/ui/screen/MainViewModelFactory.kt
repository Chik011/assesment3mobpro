package com.chiko0085.assesment3mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chiko0085.assesment3mobpro.network.UserDataStore

class MainViewModelFactory(private val dataStore: UserDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
