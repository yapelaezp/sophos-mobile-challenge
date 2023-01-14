package com.example.sophos_mobile_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("POKEEEEEEEEEEEEEEMON M ACTIVITY")
        setContentView(R.layout.activity_main)
    }

}