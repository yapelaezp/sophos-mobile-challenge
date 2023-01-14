package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.mappers.toEntity
import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.data.source.local.db.dao.OfficesDao
import com.example.sophos_mobile_app.data.source.remote.api.ApiService
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.source.makeRepositoryCall
import javax.inject.Inject

class OfficeRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val officesDao: OfficesDao
) : OfficeRepository {

    override suspend fun getOffices(): ResponseStatus<List<Office>> = makeRepositoryCall {
        val response = if (officesDao.getOffices().isEmpty()) {
            api.getOffices().Items.map { OfficeDto ->
                OfficeDto.toModel()
            }.also { offices ->
                officesDao.insertOffices(offices.map { office ->
                    office.toEntity()
                })
            }
        } else {
            officesDao.getOffices().map { officeEntity ->
                officeEntity.toModel()
            }
        }
        response
    }
}