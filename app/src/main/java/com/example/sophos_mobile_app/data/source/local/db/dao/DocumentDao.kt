package com.example.sophos_mobile_app.data.source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sophos_mobile_app.data.source.local.db.entities.DocumentDetailEntity
import com.example.sophos_mobile_app.data.source.local.db.entities.DocumentEntity

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(documents: DocumentEntity)

    @Query("SELECT * FROM documents")
    suspend fun getDocuments(): List<DocumentEntity>

    @Query("SELECT * FROM document_detail WHERE register_id = :registerId")
    suspend fun getDocumentById(registerId: String): DocumentDetailEntity

}