package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ApiService
import com.example.sophos_mobile_app.data.api.ResponseStatus
import com.example.sophos_mobile_app.data.api.makeNetworkCall
import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.Office
import javax.inject.Inject

class OfficeRepositoryImpl @Inject constructor(private val api: ApiService) : OfficeRepository {

    override suspend fun getOffices(): ResponseStatus<List<Office>> = makeNetworkCall {
        val response = api.getOffices().Items
        response.map { OfficeDto ->
            OfficeDto.toModel()
        }
    }

}