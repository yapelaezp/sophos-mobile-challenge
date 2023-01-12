package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.source.remote.api.ApiService
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.source.remote.api.dto.NewDocumentDto
import com.example.sophos_mobile_app.data.source.remote.api.makeRepositoryCall
import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.model.DocumentDetail
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(val api: ApiService) : DocumentRepository {

    override suspend fun createNewDocument(
        idType: String,
        identification: String,
        name: String,
        lastname: String,
        city: String,
        email: String,
        attachedType: String,
        attached: String
    ): ResponseStatus<Boolean> = makeRepositoryCall {
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
        val response = api.createNewDocument(newDocument)
        response.put
    }

    override suspend fun getDocumentsByUserEmail(email: String): ResponseStatus<List<Document>> =
        makeRepositoryCall {
            val response = api.getDocumentByUserEmail(email).Items.map { document ->
                document.toModel()
            }
            response
        }

    override suspend fun getDocumentDetail(registerId: String): ResponseStatus<List<DocumentDetail>> =
        makeRepositoryCall {
            val response = api.getDocumentDetail(registerId).Items.map { documentDetailDto ->
                documentDetailDto.toModel()
            }
            response
        }
}