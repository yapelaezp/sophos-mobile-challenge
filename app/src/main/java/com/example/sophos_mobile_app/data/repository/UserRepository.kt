package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.model.User
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus

interface UserRepository {
    suspend fun getUserById(email: String, password: String): ResponseStatus<User>
}