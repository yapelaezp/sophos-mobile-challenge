package com.example.sophos_mobile_app.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sophos_mobile_app.data.source.local.db.dao.DocumentDao
import com.example.sophos_mobile_app.data.source.local.db.dao.OfficesDao
import com.example.sophos_mobile_app.data.source.local.db.entities.DocumentDetailEntity
import com.example.sophos_mobile_app.data.source.local.db.entities.DocumentEntity
import com.example.sophos_mobile_app.data.source.local.db.entities.OfficeEntity
import com.example.sophos_mobile_app.utils.DATABASE_NAME

@Database(
    entities = [DocumentEntity::class, DocumentDetailEntity::class, OfficeEntity::class],
    version = 1
)
abstract class SophosAppDatabase : RoomDatabase() {

    companion object{
        @Volatile
        private var INSTANCE : SophosAppDatabase? = null

        fun getDatabase(context: Context): SophosAppDatabase {
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SophosAppDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }


    abstract fun getDocumentDao(): DocumentDao
    abstract fun getOfficeDao(): OfficesDao

}