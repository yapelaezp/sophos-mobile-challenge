package com.example.sophos_mobile_app.data.source.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document_detail")
data class DocumentDetailEntity(
    val attached: String,
    val lastname: String,
    val city: String,
    val email: String,
    val date: String,
    @PrimaryKey @ColumnInfo(name = "register_id") val registerId: String,
    val id: String,
    val name: String,
    @ColumnInfo(name = "attached_type") val attachedType: String,
    val idType: String
)
