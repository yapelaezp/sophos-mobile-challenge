package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.model.DocumentDetail

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
    ): ResponseStatus<Boolean>

    suspend fun getDocuments(email: String): ResponseStatus<List<Document>>

    suspend fun getDocumentDetail(registerId: String): ResponseStatus<List<DocumentDetail>>
}