package com.example.sophos_mobile_app.data.api.dto

data class OfficesResponse(
    val Count: Int,
    val Items: List<OfficeDto>,
    val ScannedCount: Int
)