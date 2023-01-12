package com.example.sophos_mobile_app.data.source.remote.api.responses

import com.example.sophos_mobile_app.data.source.remote.api.dto.DocumentDetailDto

data class DocumentDetailResponse(
    val Count: Int,
    val Items: List<DocumentDetailDto>,
    val ScannedCount: Int
)