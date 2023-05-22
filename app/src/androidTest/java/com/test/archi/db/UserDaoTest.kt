package com.test.archi.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.archi.common.TestUtil
import com.test.archi.common.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndLoad() {
        val user = TestUtil.createUser("foo")
        db.userDao().insert(user)

        val loaded = db.userDao().findByLogin(user.login).getOrAwaitValue()
        assertThat(loaded.login, `is`("foo"))

        val replacement = TestUtil.createUser("foo2")
        db.userDao().insert(replacement)

        val loadedReplacement = db.userDao().findByLogin(replacement.login).getOrAwaitValue()
        assertThat(loadedReplacement.login, `is`("foo2"))
    }
}
