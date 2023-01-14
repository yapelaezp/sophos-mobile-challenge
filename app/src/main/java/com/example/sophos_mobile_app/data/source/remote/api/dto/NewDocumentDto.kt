package com.example.sophos_mobile_app.data.source.remote.api.dto

import com.google.gson.annotations.SerializedName

data class NewDocumentDto (
    @SerializedName("TipoId") val idType: String,
    @SerializedName("Identificacion") val identification: String,
    @SerializedName("Nombre") val name: String,
    @SerializedName("Apellido") val lastname: String,
    @SerializedName("Ciudad") val city: String,
    @SerializedName("Correo") val email: String,
    @SerializedName("TipoAdjunto") val attachedType: String,
    @SerializedName("Adjunto") val attached: String
)