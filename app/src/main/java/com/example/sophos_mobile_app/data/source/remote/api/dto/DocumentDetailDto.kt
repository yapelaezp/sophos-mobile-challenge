package com.example.sophos_mobile_app.data.source.remote.api.dto

import com.google.gson.annotations.SerializedName

data class DocumentDetailDto(
    @SerializedName("Adjunto") val attached:String,
    @SerializedName("Apellido") val lastname:String,
    @SerializedName("Ciudad") val city:String,
    @SerializedName("Correo") val email:String,
    @SerializedName("Fecha") val date:String,
    @SerializedName("IdRegistro") val registerId:String,
    @SerializedName("Identificacion") val id:String,
    @SerializedName("Nombre") val name:String,
    @SerializedName("TipoAdjunto") val attachedType:String,
    @SerializedName("TipoId") val idType:String
)