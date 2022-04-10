package com.example.core.di

import com.example.core.data.api.BaseUrlProvider
import com.example.core.data.api.BaseUrlProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BaseUrlPoviderModule {
    @Provides
    @Singleton
    fun provideUrlProvider(): BaseUrlProvider = BaseUrlProviderImpl()
}
