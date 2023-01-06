package com.example.sophos_mobile_app.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class DocumentDetail (
    val attached: String,
    val lastname: String,
    val city: String,
    val email: String,
    val date: String,
    val registerId: String,
    val id: String,
    val name: String,
    val attachedType: String,
    val idType: String
)