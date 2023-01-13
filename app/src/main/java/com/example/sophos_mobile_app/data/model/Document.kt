package com.example.sophos_mobile_app.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneOffset

data class Document (
    val lastname: String,
    val date: String,
    val logId: String,
    val name: String,
    val attachedType: String
){
    val customDate: String @RequiresApi(Build.VERSION_CODES.O)
    get() = try {
            Instant.parse(date).atZone(ZoneOffset.UTC).let {
                val v =  it.dayOfMonth.toString() + "/" + it.monthValue + "/" + it.year.toString()
                v
            }.toString()
    } catch (e: Exception){
            this.date
    }
}