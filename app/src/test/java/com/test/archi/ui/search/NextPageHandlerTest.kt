package com.test.archi.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.test.archi.repos.RepoRepository
import com.test.archi.ui.search.SearchViewModel
import com.test.archi.vo.Resource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

class NextPageHandlerTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(RepoRepository::class.java)

    private lateinit var pageHandler: SearchViewModel.NextPageHandler

    @Before
    fun init() {
        pageHandler = SearchViewModel.NextPageHandler(repository)
    }

    private val status: SearchViewModel.LoadMoreState?
        get() = pageHandler.loadMoreState.value

    @Test
    fun constructor() {
        val initial = status
        assertThat<SearchViewModel.LoadMoreState>(initial, notNullValue())
        assertThat(initial?.isRunning, `is`(false))
        assertThat(initial?.errorMessage, nullValue())
    }

}
