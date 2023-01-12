package com.example.sophos_mobile_app.data.source.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val lastname: String?,
    val access: Boolean?,
    val admin: Boolean?
)