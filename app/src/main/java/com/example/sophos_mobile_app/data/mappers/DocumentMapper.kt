package com.example.sophos_mobile_app.data.mappers

import com.example.sophos_mobile_app.data.api.dto.DocumentDto
import com.example.sophos_mobile_app.data.model.Document

fun DocumentDto.toModel() = Document(
    lastname, date, logId, name, attachedType
)