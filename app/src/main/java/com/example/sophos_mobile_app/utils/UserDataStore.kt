package com.example.sophos_mobile_app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.sophos_mobile_app.data.model.UserPreferences
import com.example.sophos_mobile_app.ui.login.LoginFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class UserDataStore(private val context: Context) {

    suspend fun getDataStorePreferences() = withContext(Dispatchers.IO){
        context.dataStore.data.map { preferences ->
            UserPreferences(
                email = preferences[stringPreferencesKey(LoginFragment.EMAIL)].orEmpty(),
                password = preferences[stringPreferencesKey(LoginFragment.PASSWORD)].orEmpty(),
                name = preferences[stringPreferencesKey(LoginFragment.NAME)].orEmpty(),
                biometricIntention = preferences[booleanPreferencesKey(LoginFragment.BIOMETRIC_INTENTION)]
                    ?: false,
                biometricEmail = preferences[stringPreferencesKey(LoginFragment.BIOMETRIC_EMAIL)].orEmpty(),
                biometricPassword = preferences[stringPreferencesKey(LoginFragment.BIOMETRIC_PASSWORD)].orEmpty()
            )
        }
    }

    suspend fun saveUserInDataStore(email: String, password: String, name: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(LoginFragment.EMAIL)] = email
            preferences[stringPreferencesKey(LoginFragment.PASSWORD)] = password
            preferences[stringPreferencesKey(LoginFragment.NAME)] = name
        }
    }

    suspend fun setBiometricIntention() {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(LoginFragment.BIOMETRIC_INTENTION)] = true
        }
    }

    suspend fun saveBiometricData(userEmail: String, userPassword: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(LoginFragment.BIOMETRIC_EMAIL)] = userEmail
            preferences[stringPreferencesKey(LoginFragment.BIOMETRIC_PASSWORD)] = userPassword
        }
    }

}


