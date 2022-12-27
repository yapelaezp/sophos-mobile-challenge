package com.example.sophos_mobile_app.data.api.responses

import com.example.sophos_mobile_app.data.api.dto.DocumentDetailDto

data class DocumentDetailResponse(
    val Count: Int,
    val Items: List<DocumentDetailDto>,
    val ScannedCount: Int
)