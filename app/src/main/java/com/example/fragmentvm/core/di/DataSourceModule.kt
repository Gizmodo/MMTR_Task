package com.example.fragmentvm.core.di

import com.example.fragmentvm.data.DataStoreRepository
import com.example.fragmentvm.domain.DataStoreInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideDataStoreRepository(ds: DataStoreRepository): DataStoreInterface = ds
}