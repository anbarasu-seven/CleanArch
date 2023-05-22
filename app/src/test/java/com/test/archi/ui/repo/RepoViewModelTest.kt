package com.test.archi.ui.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.test.archi.util.mock
import com.test.archi.vo.Repo
import com.test.archi.repos.RepoRepository
import com.test.archi.vo.Contributor
import com.test.archi.vo.Resource
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@RunWith(JUnit4::class)
class RepoViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(RepoRepository::class.java)
    private var repoViewModel = RepoViewModel(repository)


    @Test
    fun resetId() {
        val observer = mock<Observer<RepoViewModel.RepoId>>()
        repoViewModel.repoId.observeForever(observer)
        verifyNoMoreInteractions(observer)
        repoViewModel.setId("foo", "bar")
        verify(observer).onChanged(RepoViewModel.RepoId("foo", "bar"))
        reset(observer)
        repoViewModel.setId("foo", "bar")
        verifyNoMoreInteractions(observer)
        repoViewModel.setId("a", "b")
        verify(observer).onChanged(RepoViewModel.RepoId("a", "b"))
    }


    @Test
    fun blankRepoId() {
        repoViewModel.setId("", "")
        val observer1 = mock<Observer<Resource<Repo>>>()
        val observer2 = mock<Observer<Resource<List<Contributor>>>>()
        repoViewModel.repo.observeForever(observer1)
        repoViewModel.contributors.observeForever(observer2)
        verify(observer1).onChanged(null)
        verify(observer2).onChanged(null)
    }
}