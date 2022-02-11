package com.example.fragmentvm.di

import com.example.fragmentvm.repository.data.DataStoreRepository
import com.example.fragmentvm.repository.data.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideDataStoreRepository(ds: DataStoreRepositoryImpl): DataStoreRepository = ds
}