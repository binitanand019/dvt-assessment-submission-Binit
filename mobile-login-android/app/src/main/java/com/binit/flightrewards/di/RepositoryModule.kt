package com.binit.flightrewards.di

import com.binit.flightrewards.data.remote.AuthApi
import com.binit.flightrewards.data.repository.AuthRepository
import com.binit.flightrewards.data.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi
    ): AuthRepository {

        return AuthRepositoryImpl(api)
    }
}