package com.example.fragmentvm.di

import com.example.fragmentvm.data.DataStoreRepository
import com.example.fragmentvm.data.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideDataStoreRepository(ds: DataStoreRepositoryImpl): DataStoreRepository = ds
}