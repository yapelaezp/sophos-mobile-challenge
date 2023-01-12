package com.example.sophos_mobile_app.data.mappers

import com.example.sophos_mobile_app.data.source.remote.api.dto.DocumentDetailDto
import com.example.sophos_mobile_app.data.source.remote.api.dto.DocumentDto
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.model.DocumentDetail
import com.example.sophos_mobile_app.data.source.local.db.entities.DocumentEntity

fun DocumentDto.toModel() = Document(
    lastname, date, logId, name, attachedType
)

fun DocumentDetailDto.toModel() = DocumentDetail(
    attached,
    lastname,
    city,
    email,
    date,
    registerId,
    id,
    name,
    attachedType,
    idType
)

fun DocumentEntity.toModel() = Document(
    lastname, date, logId, name, attachedType
)

fun Document.toEntity() = DocumentEntity(
    lastname, date, logId, name, attachedType
)
