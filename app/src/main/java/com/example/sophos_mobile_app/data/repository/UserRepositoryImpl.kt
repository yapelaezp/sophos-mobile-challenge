package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.User
import com.example.sophos_mobile_app.data.source.remote.api.ApiService
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.source.remote.api.makeRepositoryCall
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserRepository {

    override suspend fun getUserById(email: String, password: String): ResponseStatus<User> =
        makeRepositoryCall {
            val response = api.getUserById(email, password).toModel()
            response
        }

}