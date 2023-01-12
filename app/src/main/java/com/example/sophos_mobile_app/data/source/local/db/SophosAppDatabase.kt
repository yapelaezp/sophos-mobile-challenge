package com.example.sophos_mobile_app.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sophos_mobile_app.data.source.local.db.dao.DocumentDao
import com.example.sophos_mobile_app.data.source.local.db.dao.OfficesDao
import com.example.sophos_mobile_app.data.source.local.db.dao.UserDao
import com.example.sophos_mobile_app.data.source.local.db.entities.DocumentDetailEntity
import com.example.sophos_mobile_app.data.source.local.db.entities.DocumentEntity
import com.example.sophos_mobile_app.data.source.local.db.entities.OfficeEntity
import com.example.sophos_mobile_app.data.source.local.db.entities.UserEntity

@Database(entities = [UserEntity::class, DocumentEntity::class, DocumentDetailEntity::class, OfficeEntity::class], version = 1)
abstract class SophosAppDatabase: RoomDatabase() {

    abstract fun getDocumentDao(): DocumentDao
    abstract fun getUserDao(): UserDao
    abstract fun getOfficeDao(): OfficesDao

}