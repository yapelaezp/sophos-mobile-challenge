package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ApiService
import com.example.sophos_mobile_app.data.api.dto.NewDocumentDto
import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: ApiService) : UserRepository {

    override suspend fun getUserById(email: String, password: String): User {
        try {
            return api.getUserById(email, password).toModel()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

}