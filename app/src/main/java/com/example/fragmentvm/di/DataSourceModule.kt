package com.example.fragmentvm.di

import dagger.Module
import dagger.Provides
import ru.mmtr.data.api.network.data.DataStoreRepository
import ru.mmtr.data.api.network.data.DataStoreRepositoryImpl
import javax.inject.Singleton

@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideDataStoreRepository(ds: DataStoreRepositoryImpl): DataStoreRepository = ds
}