package com.test.archi.ui.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.test.archi.util.TestUtil
import com.test.archi.util.mock
import com.test.archi.vo.Repo
import com.test.archi.repos.RepoRepository
import com.test.archi.repos.UserRepository
import com.test.archi.vo.Resource
import com.test.archi.vo.User
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@RunWith(JUnit4::class)
class UserViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val userRepository = mock(UserRepository::class.java)
    private val repoRepository = mock(RepoRepository::class.java)
    private val userViewModel = UserViewModel(userRepository, repoRepository)

    @Test
    fun nullUser() {
        val observer = mock<Observer<Resource<User>>>()
        userViewModel.setLogin("foo")
        userViewModel.setLogin(null)
        userViewModel.user.observeForever(observer)
        verify(observer).onChanged(null)
    }


    @Test
    fun dontRefreshOnSameData() {
        val observer = mock<Observer<String?>>()
        userViewModel.login.observeForever(observer)
        verifyNoMoreInteractions(observer)
        userViewModel.setLogin("foo")
        verify(observer).onChanged("foo")
        reset(observer)
        userViewModel.setLogin("foo")
        verifyNoMoreInteractions(observer)
        userViewModel.setLogin("bar")
        verify(observer).onChanged("bar")
    }

    @Test
    fun noRetryWithoutUser() {
        userViewModel.retry()
        verifyNoMoreInteractions(userRepository, repoRepository)
    }
}