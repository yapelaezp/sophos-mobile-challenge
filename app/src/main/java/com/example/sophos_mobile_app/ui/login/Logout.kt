package com.example.sophos_mobile_app.ui.login

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.ui.menu.MenuFragmentDirections
import com.example.sophos_mobile_app.utils.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Logout(private val fragment: Fragment) {
     suspend fun logout() {
        withContext(Dispatchers.IO){
            fragment.requireContext().dataStore.edit { preferences ->
                preferences[stringPreferencesKey(LoginFragment.EMAIL)] = ""
                preferences[stringPreferencesKey(LoginFragment.PASSWORD)] = ""
                preferences[stringPreferencesKey(LoginFragment.NAME)] = ""
            }
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(fragment.requireContext(), "Data cleared successfully", Toast.LENGTH_SHORT).show()
/*            val action =
                fragment.findNavController().navigate(fragment.)
                MenuFragmentDirections.actionMenuFragmentDestinationToLoginFragmentDestination()
                fragment.findNavController().navigate(action)*/
        }
    }
}