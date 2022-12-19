package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.model.Document

interface DocumentRepository {
    suspend fun createNewDocument(
        idType: String,
        identification: String,
        name: String,
        lastname: String,
        city: String,
        email: String,
        attachedType: String,
        attached: String
    )

    suspend fun getDocumentByUserEmail(email: String): List<Document>
}