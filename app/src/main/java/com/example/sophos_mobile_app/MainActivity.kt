package com.example.sophos_mobile_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
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
        setContentView(R.layout.activity_main)
        userDataStore = UserDataStore(this)
        setAppMode()
    }

    private fun setAppMode() {
        lifecycleScope.launch(Dispatchers.IO){
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