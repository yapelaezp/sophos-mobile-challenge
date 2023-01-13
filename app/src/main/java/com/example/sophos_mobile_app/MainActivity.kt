package com.example.sophos_mobile_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.sophos_mobile_app.data.source.local.db.SophosAppDatabase
import com.example.sophos_mobile_app.utils.UserDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("POKEEEEEEEEEEEEEEMON M ACTIVITY")
        setContentView(R.layout.activity_main)
    }

}