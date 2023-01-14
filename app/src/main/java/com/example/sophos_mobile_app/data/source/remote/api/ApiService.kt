package com.example.sophos_mobile_app.data.source.remote.api

import com.example.sophos_mobile_app.data.source.remote.api.dto.NewDocumentDto
import com.example.sophos_mobile_app.data.source.remote.api.responses.OfficesResponse
import com.example.sophos_mobile_app.data.source.remote.api.dto.UserDto
import com.example.sophos_mobile_app.data.source.remote.api.responses.DocumentDetailResponse
import com.example.sophos_mobile_app.data.source.remote.api.responses.DocumentResponse
import com.example.sophos_mobile_app.data.source.remote.api.responses.NewDocumentResponse
import com.example.sophos_mobile_app.utils.DOCUMENTS_URL
import com.example.sophos_mobile_app.utils.OFFICES_URL
import com.example.sophos_mobile_app.utils.USERS_URL
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET(USERS_URL)
    suspend fun getUserById(
        @Query("idUsuario") userId: String,
        @Query("clave") password: String
    ): UserDto

    @GET(OFFICES_URL)
    suspend fun getOffices(): OfficesResponse

    @POST(DOCUMENTS_URL)
    suspend fun createNewDocument(@Body newDocumentDto: NewDocumentDto): NewDocumentResponse

    @GET(DOCUMENTS_URL)
    suspend fun getDocuments(@Query("correo") email: String): DocumentResponse

    @GET(DOCUMENTS_URL)
    suspend fun getDocumentDetail(@Query("idRegistro") registerId: String): DocumentDetailResponse
}