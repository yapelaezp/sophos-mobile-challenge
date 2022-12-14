package com.example.sophos_mobile_app.data.mappers

import com.example.sophos_mobile_app.data.api.dto.UserDto
import com.example.sophos_mobile_app.data.model.User

fun UserDto.toModel() = User(
    access =  acceso,
    admin = admin,
    lastname = apellido,
    id = id,
    name = nombre
)

