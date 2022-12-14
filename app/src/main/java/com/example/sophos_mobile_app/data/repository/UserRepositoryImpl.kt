package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ApiService
import com.example.sophos_mobile_app.data.api.ProvideRetrofit
import com.example.sophos_mobile_app.data.api.dto.UserDto
import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: ApiService): UserRepository {

    override suspend fun getUserById(email: String, password: String): User {
        return withContext(Dispatchers.IO){
            val response = api.getUserById(email, password).toModel()
            response
        }
    }
    
}