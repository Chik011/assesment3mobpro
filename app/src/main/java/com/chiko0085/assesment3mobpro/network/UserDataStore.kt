package com.chiko0085.assesment3mobpro.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chiko0085.assesment3mobpro.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserDataStore(private val context: Context) {
    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PHOTO = stringPreferencesKey("user_photo")
    }

    val userFlow: Flow<User?> = context.dataStore.data.map { preferences ->
        val name = preferences[USER_NAME] ?: return@map null
        val email = preferences[USER_EMAIL] ?: return@map null
        val photo = preferences[USER_PHOTO] ?: return@map null
        User(name, email, photo)
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_EMAIL] = user.email
            preferences[USER_PHOTO] = user.photoUrl
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
