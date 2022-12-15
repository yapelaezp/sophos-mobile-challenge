package com.example.sophos_mobile_app.data.repository

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
}