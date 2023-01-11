package com.example.sophos_mobile_app.data.repository

import com.example.sophos_mobile_app.data.api.ResponseStatus
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.model.DocumentDetail

class FakeDocumentRepository: DocumentRepository {
    override suspend fun createNewDocument(
        idType: String,
        identification: String,
        name: String,
        lastname: String,
        city: String,
        email: String,
        attachedType: String,
        attached: String
    ): ResponseStatus<Boolean> {
        return ResponseStatus.Success(true)
    }

    override suspend fun getDocumentsByUserEmail(email: String): ResponseStatus<List<Document>> {
        return if (email == "alejo51120@gmail.com") {
            ResponseStatus.Success(
                listOf(
                    Document(
                        "Pelaez Posada",
                        "2022-12-27T03:53:10.184Z",
                        "c34de8dc-fcf0-461d-8513-12a525505df8",
                        "Alejandro", "Incapacidad"
                    ),
                    Document(
                        "Pelaez Posada",
                        "2022-12-28T03:53:10.184Z",
                        "c34de8dc-fcf0-461d-aaaa--bbbb",
                        "Alejandro", "Incapacidad"
                    )
                )
            )
        } else {
            ResponseStatus.Success(emptyList())
        }
    }

    override suspend fun getDocumentDetail(registerId: String): ResponseStatus<List<DocumentDetail>> {
        return ResponseStatus.Success(emptyList())
    }
}




