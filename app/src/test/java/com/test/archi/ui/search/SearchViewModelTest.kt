package com.test.archi.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.test.archi.util.mock
import com.test.archi.vo.Repo
import com.test.archi.repos.RepoRepository
import com.test.archi.vo.Resource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@RunWith(JUnit4::class)
class SearchViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val repository = mock(RepoRepository::class.java)
    private lateinit var viewModel: SearchViewModel

    @Before
    fun init() {
        // need to init after instant executor rule is established.
        viewModel = SearchViewModel(repository)
    }

    @Test
    fun empty() {
        val result = mock<Observer<Resource<List<Repo>>>>()
        viewModel.results.observeForever(result)
        viewModel.loadNextPage()
        verifyNoMoreInteractions(repository)
    }

}
