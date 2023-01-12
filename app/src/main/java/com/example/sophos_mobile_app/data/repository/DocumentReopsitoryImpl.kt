package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.mappers.toEntity
import com.example.sophos_mobile_app.data.mappers.toModel
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.model.DocumentDetail
import com.example.sophos_mobile_app.data.source.local.db.dao.DocumentDao
import com.example.sophos_mobile_app.data.source.remote.api.ApiService
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.data.source.remote.api.dto.NewDocumentDto
import com.example.sophos_mobile_app.data.source.remote.api.makeRepositoryCall
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val documentsDao: DocumentDao
) : DocumentRepository {

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

    override suspend fun getDocuments(email: String): ResponseStatus<List<Document>> =
        makeRepositoryCall {
            val response = if (documentsDao.getDocuments().isEmpty()){
                api.getDocuments(email).Items.map { documentDto ->
                    documentDto.toModel()
                }.also { documentList ->
                       documentsDao.insertDocuments(  documentList.map { document ->
                           document.toEntity()
                       })
                }
            } else {
                documentsDao.getDocuments().map { documentEntity ->
                    documentEntity.toModel()
                }
            }
            response
        }

    override suspend fun getDocumentDetail(registerId: String): ResponseStatus<List<DocumentDetail>> =
        makeRepositoryCall {
            val response = if(documentsDao.getDocumentDetail(registerId).isEmpty()){
                api.getDocumentDetail(registerId).Items.map{ documentDetailDto ->
                    documentDetailDto.toModel()
                }.also { documentDetailList ->
                    documentsDao.insertDocumentDetail(documentDetailList.map {  documentDetail ->
                        documentDetail.toEntity()
                    })
                }
            } else {
                documentsDao.getDocumentDetail(registerId).map { documentDetailEntity ->
                    documentDetailEntity.toModel()
                }
            }
            response
        }
}