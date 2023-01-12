package com.example.sophos_mobile_app.di

import com.example.sophos_mobile_app.data.source.remote.api.ApiService
import com.example.sophos_mobile_app.data.repository.*
import com.example.sophos_mobile_app.utils.BASE_URL
import com.example.sophos_mobile_app.utils.ConvertidorImagenes
import com.example.sophos_mobile_app.utils.ImageConverter
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideApi(): ApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideDocumentRepository(apiService: ApiService): DocumentRepository {
        return DocumentRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideOfficeRepository(apiService: ApiService): OfficeRepository {
        return OfficeRepositoryImpl(apiService)
    }

}