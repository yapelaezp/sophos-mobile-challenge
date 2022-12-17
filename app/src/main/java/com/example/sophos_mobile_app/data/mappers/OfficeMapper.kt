package com.example.sophos_mobile_app.data.mappers

import com.example.sophos_mobile_app.data.api.dto.OfficeDto
import com.example.sophos_mobile_app.data.model.Office

fun OfficeDto.toModel() = Office(
    city, officeId, latitude, longitude, name
)