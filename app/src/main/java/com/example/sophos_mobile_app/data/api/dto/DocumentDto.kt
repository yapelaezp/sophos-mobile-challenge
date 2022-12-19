package com.example.sophos_mobile_app.data.api.dto

import com.google.gson.annotations.SerializedName

data class DocumentDto(
    @SerializedName("Apellido") val lastname: String,
    @SerializedName("Fecha") val date: String,
    @SerializedName("IdRegistro") val logId: String,
    @SerializedName("Nombre") val name: String,
    @SerializedName("TipoAdjunto") val attachedType: String
)