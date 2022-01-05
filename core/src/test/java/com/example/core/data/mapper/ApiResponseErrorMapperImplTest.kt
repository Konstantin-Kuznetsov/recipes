package com.example.core.data.mapper

import com.example.core.data.model.RecipesError
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*

import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

@RunWith(Enclosed::class)
class ApiResponseErrorMapperImplTest {

    @RunWith(Parameterized::class)
    class ApiResponseErrorMapperParametrizedTest(
        private val error: Throwable,
        private val correctlyMappedError: RecipesError
    ) {
        @Test
        fun mapApiErrorToDomain() {
            assertEquals(
                "Wrong error mapping result!", correctlyMappedError,
                mapper.map(error)
            )
        }

        companion object {
            private val mapper = ApiResponseErrorMapperImpl()
            private val unknownHostException = UnknownHostException()
            private val unauthorizedException =
                HttpException(Response.error<Any>(401, "test-error-body".toResponseBody(null)))
            private val exception500 =
                HttpException(Response.error<Any>(500, "test-error-body".toResponseBody(null)))
            private val nonHttpException = IOException()

            @JvmStatic
            @Parameterized.Parameters(name = "{index}: Mapping result of {0} is {1}")
            fun testData() = listOf(
                arrayOf(unknownHostException, RecipesError.NoInternetError),
                arrayOf(unauthorizedException, RecipesError.Unauthorized),
                arrayOf(exception500, RecipesError.ApiError(exception500)),
                arrayOf(nonHttpException, RecipesError.UnknownError(nonHttpException))
            )
        }
    }
}
