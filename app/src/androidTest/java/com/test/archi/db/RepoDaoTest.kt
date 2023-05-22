package com.test.archi.db

import android.database.sqlite.SQLiteException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.test.archi.common.TestUtil;
import com.test.archi.common.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

@RunWith(AndroidJUnit4::class)
class RepoDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndRead() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        db.repoDao().insert(repo)
        val loaded = db.repoDao().load("foo", "bar").getOrAwaitValue()
        checkNotNull(loaded)
        assertEquals(loaded.name, "bar")
        assertEquals(loaded.description, "desc")
        assertNotNull(loaded.owner,)
        assertEquals(loaded.owner.login, `is`("foo"))
    }

    @Test
    fun insertContributorsWithoutRepo() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        val contributor = TestUtil.createContributor(repo, "c1", 3)
        try {
            db.repoDao().insertContributors(listOf(contributor))
            throw AssertionError("must fail because repo does not exist")
        } catch (ex: SQLiteException) {
        }

    }

    @Test
    fun insertContributors() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        val c1 = TestUtil.createContributor(repo, "c1", 3)
        val c2 = TestUtil.createContributor(repo, "c2", 7)
        db.runInTransaction {
            db.repoDao().insert(repo)
            db.repoDao().insertContributors(arrayListOf(c1, c2))
        }
        val list = db.repoDao().loadContributors("foo", "bar").getOrAwaitValue()
        assertThat(list.size, `is`(2))
        val first = list[0]

        assertThat(first.login, `is`("c2"))
        assertThat(first.contributions, `is`(7))

        val second = list[1]
        assertThat(second.login, `is`("c1"))
        assertThat(second.contributions, `is`(3))
    }

    @Test
    fun createIfNotExists_exists() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        db.repoDao().insert(repo)
        assertThat(db.repoDao().createRepoIfNotExists(repo), `is`(-1L))
    }

    @Test
    fun createIfNotExists_doesNotExist() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        assertThat(db.repoDao().createRepoIfNotExists(repo), `is`(1L))
    }

    @Test
    fun insertContributorsThenUpdateRepo() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        db.repoDao().insert(repo)
        val contributor = TestUtil.createContributor(repo, "aa", 3)
        db.repoDao().insertContributors(listOf(contributor))
        var data = db.repoDao().loadContributors("foo", "bar")
        assertThat(data.getOrAwaitValue().size, `is`(1))

        val update = TestUtil.createRepo("foo", "bar", "desc")
        db.repoDao().insert(update)
        data = db.repoDao().loadContributors("foo", "bar")
        assertThat(data.getOrAwaitValue().size, `is`(1))
    }
}
