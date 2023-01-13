package com.example.sophos_mobile_app.di

import com.example.sophos_mobile_app.data.repository.*
import com.example.sophos_mobile_app.data.source.local.db.dao.DocumentDao
import com.example.sophos_mobile_app.data.source.local.db.dao.OfficesDao
import com.example.sophos_mobile_app.data.source.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideDocumentRepository(apiService: ApiService, documentDao: DocumentDao): DocumentRepository {
        return DocumentRepositoryImpl(apiService, documentDao)
    }

    @Provides
    @Singleton
    fun provideOfficeRepository(apiService: ApiService, officesDao: OfficesDao): OfficeRepository {
        return OfficeRepositoryImpl(apiService, officesDao)
    }
}