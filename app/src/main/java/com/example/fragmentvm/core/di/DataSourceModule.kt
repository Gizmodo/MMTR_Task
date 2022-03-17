package com.example.fragmentvm.core.di

import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.data.DataStoreRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideDataStoreRepository(ds: DataStoreRepository): DataStoreInterface = ds
}