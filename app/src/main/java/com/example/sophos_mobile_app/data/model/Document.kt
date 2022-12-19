package com.example.sophos_mobile_app.data.model

import com.google.gson.annotations.SerializedName

data class Document (
    val lastname: String,
    val date: String,
    val logId: String,
    val name: String,
    val attachedType: String
)