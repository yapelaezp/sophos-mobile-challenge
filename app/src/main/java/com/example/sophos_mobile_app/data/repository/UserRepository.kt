package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.User

interface UserRepository {
    suspend fun getUserById(email: String, password: String): ResponseStatus<User>
}