package com.example.sophos_mobile_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.ui.login.LoginFragmentDirections
import com.example.sophos_mobile_app.utils.UserDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var userDataStore: UserDataStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDataStore = UserDataStore(this)
        setAppMode()
        setContentView(R.layout.activity_main)
    }

    private fun setAppMode() {
        lifecycleScope.launch(Dispatchers.Main){
            userDataStore.getDataStorePreferences().collect{ userPreferences ->
                if (userPreferences.darkMode){
                    withContext(Dispatchers.Main){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        delegate.applyDayNight()
                    }
                }
            }
        }
    }

}