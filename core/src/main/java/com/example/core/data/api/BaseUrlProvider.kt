package com.example.core.data.api

import com.example.core.data.api.BaseUrlProviderImpl.UrlConstants.BASE_RECIPES_URL

interface BaseUrlProvider {
    val recipesBaseUrl: String
}

class BaseUrlProviderImpl: BaseUrlProvider {
    override val recipesBaseUrl: String
        get() = BASE_RECIPES_URL

    private object UrlConstants {
        const val BASE_RECIPES_URL = "https://hf-android-app.s3-eu-west-1.amazonaws.com/android-test/"
    }
}
