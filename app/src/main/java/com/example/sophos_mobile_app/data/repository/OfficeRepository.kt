package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.model.Office

interface OfficeRepository {

    suspend fun getOffices(): List<Office>

}