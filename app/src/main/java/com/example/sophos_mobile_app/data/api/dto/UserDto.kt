package com.example.sophos_mobile_app.data.api.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    val acceso: Boolean,
    val admin: Boolean,
    val apellido: String,
    val id: String,
    val nombre: String
)
