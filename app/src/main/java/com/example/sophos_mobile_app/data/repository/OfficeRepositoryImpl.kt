package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ApiService
import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.Office
import javax.inject.Inject

class OfficeRepositoryImpl @Inject constructor(private val api: ApiService): OfficeRepository {

    override suspend fun getOffices(): List<Office> {
        try {
            val response = api.getOffices().Items
            return response.map { OfficeDto -> OfficeDto.toModel() }
        } catch (e: Exception){
            throw Exception(e.message)
        }
    }

}