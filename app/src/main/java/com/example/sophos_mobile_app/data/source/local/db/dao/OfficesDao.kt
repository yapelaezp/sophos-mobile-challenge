package com.example.sophos_mobile_app.data.source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sophos_mobile_app.data.source.local.db.entities.OfficeEntity

@Dao
interface OfficesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffices(offices: List<OfficeEntity>)

    @Query("SELECT * FROM offices")
    suspend fun getOffices(): List<OfficeEntity>

}