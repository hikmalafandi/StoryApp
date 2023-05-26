package com.example.storyapp.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){


    fun getUser(): Flow<DataUser> {
        return dataStore.data.map { preferences ->
            DataUser(
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[PASSWORD_KEY] ?:"",
                preferences[STATUS_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(dataUser: DataUser) {
        dataStore.edit {preferences ->
            preferences[NAME_KEY] = dataUser.name
            preferences[EMAIL_KEY] = dataUser.email
            preferences[PASSWORD_KEY] = dataUser.password
            preferences[STATUS_KEY] = dataUser.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit {preferences ->
            preferences[STATUS_KEY] = true
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun logout() {
        dataStore.edit {preferences ->
            preferences[STATUS_KEY] = false
            preferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val STATUS_KEY = booleanPreferencesKey("status")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}