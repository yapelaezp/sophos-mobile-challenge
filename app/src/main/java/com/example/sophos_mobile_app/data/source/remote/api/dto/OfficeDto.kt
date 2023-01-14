package com.example.sophos_mobile_app.data.source.remote.api.dto

import com.google.gson.annotations.SerializedName

data class OfficeDto(
    @SerializedName("Ciudad") val city : String,
    @SerializedName("IdOficina") val officeId: Int,
    @SerializedName("Latitud") val latitude: String,
    @SerializedName("Longitud") val longitude: String,
    @SerializedName("Nombre") val name: String
)