package com.example.recipes.mockWebServer

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader

class RecipesMockResponseDispatcher {

    // Returns success response from mock server
    internal inner class SuccessDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/recipes.json" -> MockResponse().setResponseCode(200)
                    .setBody(getJsonContent("fake_recipes_response.json"))
                else -> MockResponse().setResponseCode(500)
            }
        }
    }

    // Returns error response from mock server
    internal inner class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(500)
        }
    }

    private fun getJsonContent(fileName: String) =
        with(InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(fileName))) {
            val resultJson = this.readText()
            this.close()
            resultJson
        }
}