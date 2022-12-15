package com.example.sophos_mobile_app.data.mappers

import com.example.sophos_mobile_app.data.api.dto.UserDto
import com.example.sophos_mobile_app.data.model.User

fun UserDto.toModel() = User(
    id = id,
    name = nombre,
    lastname = apellido,
    access =  acceso,
    admin = admin,
)

