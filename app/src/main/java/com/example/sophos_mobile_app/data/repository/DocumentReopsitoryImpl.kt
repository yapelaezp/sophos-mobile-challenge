package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ApiService
import com.example.sophos_mobile_app.data.api.dto.NewDocumentDto
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(val api: ApiService): DocumentRepository {

    override suspend fun createNewDocument(
        idType: String,
        identification: String,
        name: String,
        lastname: String,
        city: String,
        email: String,
        attachedType: String,
        attached: String
    ) {
        try {
            val newDocument = NewDocumentDto(
                idType,
                identification,
                name,
                lastname,
                city,
                email,
                attachedType,
                attached
            )
            api.createNewDocument(newDocument)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}