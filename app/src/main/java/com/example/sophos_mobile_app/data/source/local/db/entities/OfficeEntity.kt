package com.example.sophos_mobile_app.data.source.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offices")
data class OfficeEntity(
    val city: String,
    @PrimaryKey val officeId: Int,
    val latitude: String,
    val longitude: String,
    val name: String
)
