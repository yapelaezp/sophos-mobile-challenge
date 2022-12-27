package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ApiService
import com.example.sophos_mobile_app.data.api.dto.NewDocumentDto
import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.model.DocumentDetail
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

    override suspend fun getDocumentByUserEmail(email: String): List<Document> {
        try {
            val response = api.getDocumentByUserEmail(email).Items.map { document ->
                document.toModel()
            }
            return response
        } catch (e: Exception){
            throw Exception(e.message)
        }
    }

    override suspend fun getDocumentDetail(registerId: String): List<DocumentDetail> {
        try {
            val response = api.getDocumentDetail(registerId).Items.map { documentDetailDto ->
                documentDetailDto.toModel()
            }
            return response
        } catch (e: Exception){
            throw Exception(e.message)
        }
    }
}