package com.example.sophos_mobile_app.di

import android.content.Context
import androidx.room.Room
import com.example.sophos_mobile_app.data.source.local.db.SophosAppDatabase
import com.example.sophos_mobile_app.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, SophosAppDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideOfficesDao(db: SophosAppDatabase) = db.getOfficeDao()

    @Singleton
    @Provides
    fun provideDocumentsDao(db: SophosAppDatabase) = db.getDocumentDao()
}