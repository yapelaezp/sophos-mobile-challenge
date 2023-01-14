package com.example.sophos_mobile_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.sophos_mobile_app.utils.UserDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var userDataStore: UserDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDataStore = UserDataStore(this)
        val intent = Intent(this, MainActivity::class.java)
        setAppMode(intent)
    }

    private fun setAppMode(intent: Intent) {
        lifecycleScope.launch(Dispatchers.Main) {
            userDataStore.getDataStorePreferences().collect { userPreferences ->
                if (userPreferences.darkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()
                    println("POKEEEEEEEEEEEEEEMON splash dark")
                    startActivity(intent).also { finish() }
                } else {
                    println("POKEEEEEEEEEEEEEEMON splash day")
                    startActivity(intent).also { finish() }
                }
            }
        }
    }

}