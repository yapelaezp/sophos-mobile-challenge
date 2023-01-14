package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.Office

interface OfficeRepository {

    suspend fun getOffices(): ResponseStatus<List<Office>>

}