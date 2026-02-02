package com.example.studentinformationmanagement.data.local

import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

interface AuthLocalDataSource {
    val rememberMeFlow: Flow<Boolean>
    suspend fun setRememberMe(value: Boolean)
}

class AuthLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AuthLocalDataSource {
    companion object {
        val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
    }

    override val rememberMeFlow: Flow<Boolean> = dataStore.data
        .map { prefs ->
            prefs[REMEMBER_ME_KEY] ?: false
        }

    override suspend fun setRememberMe(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[REMEMBER_ME_KEY] = value
        }
    }
}