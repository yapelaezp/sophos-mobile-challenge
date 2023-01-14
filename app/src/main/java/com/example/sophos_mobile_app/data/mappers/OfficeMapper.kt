package com.example.sophos_mobile_app.data.mappers

import com.example.sophos_mobile_app.data.source.remote.api.dto.OfficeDto
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.data.source.local.db.entities.OfficeEntity

fun OfficeDto.toModel() = Office(
    city, officeId, latitude, longitude, name
)

fun OfficeEntity.toModel() = Office(
    city, officeId, latitude, longitude, name
)

fun Office.toEntity() = OfficeEntity(
    city, officeId, latitude, longitude, name
)