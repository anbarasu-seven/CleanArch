/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.archi.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.test.archi.util.ApiUtil
import com.test.archi.api.GithubService
import com.test.archi.db.UserDao
import com.test.archi.repos.UserRepository
import com.test.archi.util.InstantAppExecutors
import com.test.archi.util.TestUtil
import com.test.archi.util.mock
import com.test.archi.vo.Resource
import com.test.archi.vo.User
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

@RunWith(JUnit4::class)
class UserRepositoryTest {
    private val userDao = mock(UserDao::class.java)
    private val githubService = mock(GithubService::class.java)
    private val repo = UserRepository(InstantAppExecutors(), userDao, githubService)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun goToNetwork() {
        val dbData = MutableLiveData<User>()
        `when`(userDao!!.findByLogin("foo")).thenReturn(dbData)
        val user = TestUtil.createUser("foo")
        val call = ApiUtil.successCall(user)
        `when`(githubService!!.getUser("foo")).thenReturn(call)
        val observer = mock<Observer<Resource<User>>>()

        repo.loadUser("foo").observeForever(observer)
        verify(githubService, never()).getUser("foo")
        val updatedDbData = MutableLiveData<User>()
        `when`(userDao.findByLogin("foo")).thenReturn(updatedDbData)
        dbData.value = null
        verify(githubService).getUser("foo")
    }

    @Test
    fun dontGoToNetwork() {
        val dbData = MutableLiveData<User>()
        val user = TestUtil.createUser("foo")
        dbData.value = user
        `when`(userDao!!.findByLogin("foo")).thenReturn(dbData)
        val observer = mock<Observer<Resource<User>>>()
        repo.loadUser("foo").observeForever(observer)
        verify(githubService, never()).getUser("foo")
        verify(observer).onChanged(Resource.success(user))
    }
}