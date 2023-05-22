package com.test.archi.api

import com.test.archi.api.ApiErrorResponse
import com.test.archi.api.ApiResponse
import com.test.archi.api.ApiSuccessResponse
import okhttp3.Headers.Companion.headersOf
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class ApiResponseTest {
    @Test
    fun exception() {
        val exception = Exception("foo")
        val (errorMessage) = ApiResponse.create<String>(exception)
        assertThat<String>(errorMessage, `is`("foo"))
    }

    @Test
    fun success() {
        val apiResponse: ApiSuccessResponse<String> = ApiResponse
            .create<String>(Response.success("foo")) as ApiSuccessResponse<String>
        assertThat<String>(apiResponse.body, `is`("foo"))
        assertThat<Int>(apiResponse.nextPage, `is`(nullValue()))
    }

    @Test
    fun link() {
        val link =
            "<https://api.github.com/search/repositories?q=foo&page=2>; rel=\"next\"," +
                    " <https://api.github.com/search/repositories?q=foo&page=34>; rel=\"last\""
        val headers = headersOf("link", link)
        val response = ApiResponse.create<String>(Response.success("foo", headers))
        assertThat<Int>((response as ApiSuccessResponse).nextPage, `is`(2))
    }

    @Test
    fun badPageNumber() {
        val link = "<https://api.github.com/search/repositories?q=foo&page=dsa>; rel=\"next\""
        val headers = headersOf("link", link)
        val response = ApiResponse.create<String>(Response.success("foo", headers))
        assertThat<Int>((response as ApiSuccessResponse).nextPage, nullValue())
    }

    @Test
    fun badLinkHeader() {
        val link = "<https://api.github.com/search/repositories?q=foo&page=dsa>; relx=\"next\""
        val headers = headersOf("link", link)
        val response = ApiResponse.create<String>(Response.success("foo", headers))
        assertThat<Int>((response as ApiSuccessResponse).nextPage, nullValue())
    }

    @Test
    fun error() {
        val errorResponse = Response.error<String>(
            400,
            ResponseBody.create("application/txt".toMediaTypeOrNull(), "blah")
        )
        val (errorMessage) = ApiResponse.create<String>(errorResponse) as ApiErrorResponse<String>
        assertThat<String>(errorMessage, `is`("blah"))
    }
}