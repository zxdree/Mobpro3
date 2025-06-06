package com.adre0089.mini_projek3.network

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.adre0089.mini_projek3.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = "user_preference"
)

class UserDataStore(private  val context: Context) {
    companion object{
        private  val USER_NAME = stringPreferencesKey("name")
        private  val USER_EMAIL = stringPreferencesKey("email")
        private val USER_PHOTO =    stringPreferencesKey("photoUrl")
    }
    val userFlow: Flow<User> = context.dataStore.data.map { preferences ->
        User(
            nama = preferences[USER_NAME] ?:"",
            email = preferences[USER_EMAIL]?: "",
            photoUrl = preferences[USER_PHOTO]?: ""

        )
    }
    suspend fun savaData(user: User){
        context.dataStore.edit { preferencs ->
            preferencs[USER_NAME] = user.nama
            preferencs[USER_EMAIL] = user.email
            preferencs[USER_PHOTO] = user.photoUrl
        }
    }
}