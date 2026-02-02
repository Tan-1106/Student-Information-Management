package com.example.studentinformationmanagement.di

import com.example.studentinformationmanagement.data.local.AuthLocalDataSource
import com.example.studentinformationmanagement.data.local.AuthLocalDataSourceImpl
import com.example.studentinformationmanagement.data.remote.AuthRemoteDataSource
import com.example.studentinformationmanagement.data.remote.AuthRemoteDataSourceImpl
import com.example.studentinformationmanagement.data.repository.AuthRepositoryImpl
import com.example.studentinformationmanagement.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource
}

