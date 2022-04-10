package com.example.core.data.api

import com.example.core.data.api.FakeBaseUrlProviderImpl.FakeUrlConstants.FAKE_BASE_RECIPES_URL

class FakeBaseUrlProviderImpl: BaseUrlProvider {
    override val recipesBaseUrl: String
        get() = FAKE_BASE_RECIPES_URL

    private object FakeUrlConstants {
        const val FAKE_BASE_RECIPES_URL = "http://localhost:8080/"
    }
}
