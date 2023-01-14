package com.example.sophos_mobile_app.data.source.remote.api.responses

import com.example.sophos_mobile_app.data.source.remote.api.dto.OfficeDto

data class OfficesResponse(
    val Count: Int,
    val Items: List<OfficeDto>,
    val ScannedCount: Int
)