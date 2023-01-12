package com.example.sophos_mobile_app.data.source.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(
    val lastname: String,
    val date: String,
    @PrimaryKey @ColumnInfo(name = "log_id") val logId: String,
    val name: String,
    @ColumnInfo(name = "attached_type") val attachedType: String
)
