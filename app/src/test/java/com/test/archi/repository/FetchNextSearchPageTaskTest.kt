package com.test.archi.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.test.archi.api.GithubService
import com.test.archi.api.RepoSearchResponse
import com.test.archi.db.GithubDb
import com.test.archi.db.RepoDao
import com.test.archi.repos.FetchNextSearchPageTask
import com.test.archi.util.TestUtil
import com.test.archi.util.mock
import com.test.archi.vo.RepoSearchResult
import com.test.archi.vo.Resource
import okhttp3.Headers.Companion.headersOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

@RunWith(JUnit4::class)
class FetchNextSearchPageTaskTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: GithubService

    private lateinit var db: GithubDb

    private lateinit var repoDao: RepoDao

    private lateinit var task: FetchNextSearchPageTask

    private val observer: Observer<Resource<Boolean>?> = mock()

    @Before
    fun init() {
        service = mock(GithubService::class.java)
        db = mock(GithubDb::class.java)
        `when`(db.runInTransaction(any())).thenCallRealMethod()
        repoDao = mock(RepoDao::class.java)
        `when`(db.repoDao()).thenReturn(repoDao)
        task = FetchNextSearchPageTask("foo", service, db)
        task.liveData.observeForever(observer)
    }

    @Test
    fun withoutResult() {
        `when`(repoDao.search("foo")).thenReturn(null)
        task.run()
        verify(observer).onChanged(null)
        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun noNextPage() {
        createDbResult(null)
        task.run()
        verify(observer).onChanged(Resource.success(false))
        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun nextPageWithNull() {
        createDbResult(1)
        val repos = TestUtil.createRepos(10, "a", "b", "c")
        val result = RepoSearchResponse(10, repos)
        val call = createCall(result, null)
        `when`(service.searchRepos("foo", 1)).thenReturn(call)
        task.run()
        verify(repoDao).insertRepos(repos)
        verify(observer).onChanged(Resource.success(false))
    }

    @Test
    fun nextPageWithMore() {
        createDbResult(1)
        val repos = TestUtil.createRepos(10, "a", "b", "c")
        val result = RepoSearchResponse(10, repos)
        result.nextPage = 2
        val call = createCall(result, 2)
        `when`(service.searchRepos("foo", 1)).thenReturn(call)
        task.run()
        verify(repoDao).insertRepos(repos)
        verify(observer).onChanged(Resource.success(true))
    }

    @Test
    fun nextPageApiError() {
        createDbResult(1)
        val call = mock<Call<RepoSearchResponse>>()
        `when`(call.execute()).thenReturn(
            Response.error(
                400, "bar"
                    .toResponseBody("txt".toMediaTypeOrNull())
            )
        )
        `when`(service.searchRepos("foo", 1)).thenReturn(call)
        task.run()
        verify(observer)!!.onChanged(Resource.error("bar", true))
    }

    @Test
    fun nextPageIOError() {
        createDbResult(1)
        val call = mock<Call<RepoSearchResponse>>()
        `when`(call.execute()).thenThrow(IOException("bar"))
        `when`(service.searchRepos("foo", 1)).thenReturn(call)
        task.run()
        verify(observer)!!.onChanged(Resource.error("bar", true))
    }

    private fun createDbResult(nextPage: Int?) {
        val result = RepoSearchResult(
            "foo", emptyList(),
            0, nextPage
        )
        `when`(repoDao.findSearchResult("foo")).thenReturn(result)
    }

    private fun createCall(body: RepoSearchResponse, nextPage: Int?): Call<RepoSearchResponse> {
        val headers = if (nextPage == null)
            null
        else
            headersOf(
                "link",
                "<https://api.github.com/search/repositories?q=foo&page=" + nextPage
        + ">; rel=\"next\""
            )
        val success = if (headers == null)
            Response.success(body)
        else
            Response.success(body, headers)
        val call = mock<Call<RepoSearchResponse>>()
        `when`(call.execute()).thenReturn(success)

        return call
    }
}
