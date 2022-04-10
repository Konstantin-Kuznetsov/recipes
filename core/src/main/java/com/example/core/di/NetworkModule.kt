package com.example.core.di

import com.example.core.data.api.BaseUrlProvider
import com.example.core.data.api.RecipesApi
import com.example.core.data.mapper.ApiResponseErrorMapper
import com.example.core.data.mapper.ApiResponseErrorMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 60L

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideApi(
        client: OkHttpClient,
        urlProvider: BaseUrlProvider
    ): RecipesApi =
        createRetrofit(client, urlProvider.recipesBaseUrl)
            .create(RecipesApi::class.java)

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideResponseErrorMapper(): ApiResponseErrorMapper = ApiResponseErrorMapperImpl()

    private fun createRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
}