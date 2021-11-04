package com.example.core.di

import android.content.Context
import com.example.core.data.api.RecipesApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_RECIPES_URL = "https://hf-android-app.s3-eu-west-1.amazonaws.com/android-test/"
private const val TIMEOUT = 60L

@Module
class NetworkModule {

    @Provides
    fun provideApi(client: OkHttpClient): RecipesApi =
        createRetrofit(client, BASE_RECIPES_URL)
            .create(RecipesApi::class.java)

    @Provides
    fun provideOkHttpClient(context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    private fun createRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
}