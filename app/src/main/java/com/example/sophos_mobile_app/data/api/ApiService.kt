package com.example.sophos_mobile_app.data.api

import com.example.sophos_mobile_app.data.api.dto.UserDto
import com.example.sophos_mobile_app.utils.USERS_URL
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(USERS_URL)
    suspend fun getUserById(
        @Query("idUsuario") userId: String,
        @Query("clave") password: String
    ): UserDto
}