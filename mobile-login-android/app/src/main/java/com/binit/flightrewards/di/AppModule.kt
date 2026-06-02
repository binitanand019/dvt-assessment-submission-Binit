package com.binit.flightrewards.di

import android.content.Context
import com.binit.flightrewards.data.network.ConnectivityNetworkMonitor
import com.binit.flightrewards.data.network.NetworkMonitor
import com.binit.flightrewards.data.storage.DataStoreTokenStore
import com.binit.flightrewards.data.storage.TokenStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideTokenStore(
    @ApplicationContext context: Context
  ): TokenStore {

    return DataStoreTokenStore(context)
  }

  @Provides
  @Singleton
  fun provideNetworkMonitor(
    @ApplicationContext context: Context
  ): NetworkMonitor {

    return ConnectivityNetworkMonitor(context)
  }
}