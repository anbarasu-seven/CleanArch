package com.test.archi.ui.repo

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.archi.bind.FragmentBindingAdapters
import com.test.archi.common.TestUtil
import com.test.archi.util.*
import com.test.archi.vo.Contributor
import com.test.archi.vo.Repo
import com.test.archi.vo.Resource
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import com.test.archi.R;

@RunWith(AndroidJUnit4::class)
class RepoFragmentTest {
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule<RepoFragment>()

    private val navController = mock<NavController>()
    private val repoLiveData = MutableLiveData<Resource<Repo>>()
    private val contributorsLiveData = MutableLiveData<Resource<List<Contributor>>>()
    private lateinit var viewModel: RepoViewModel
    private lateinit var mockBindingAdapter: FragmentBindingAdapters

    @Before
    fun init() {
        viewModel = mock(RepoViewModel::class.java)
        mockBindingAdapter = mock(FragmentBindingAdapters::class.java)
        doNothing().`when`(viewModel).setId(anyString(), anyString())
        `when`(viewModel.repo).thenReturn(repoLiveData)
        `when`(viewModel.contributors).thenReturn(contributorsLiveData)
        val scenario = launchFragmentInContainer(
            RepoFragmentArgs("a", "b").toBundle()
        ) {
            RepoFragment().apply {
                appExecutors = countingAppExecutors.appExecutors
                //viewModelFactory = ViewModelUtil.createFor(viewModel)
                dataBindingComponent = object : androidx.databinding.DataBindingComponent {
                    override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                        return mockBindingAdapter
                    }
                }
            }
        }
        dataBindingIdlingResourceRule.monitorFragment(scenario)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.disableProgressBarAnimations()
        }
    }

    @Test
    fun testLoading() {
        repoLiveData.postValue(Resource.loading(null))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())))
    }

    @Test
    @Ignore("https://github.com/android/architecture-components-samples/issues/1076")
    fun testValueWhileLoading() {
        val repo = TestUtil.createRepo("yigit", "foo", "foo-bar")
        repoLiveData.postValue(Resource.loading(repo))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.name)).check(
            matches(
                withText(getString(R.string.repo_full_name, "yigit", "foo"))
            )
        )
        onView(withId(R.id.description)).check(matches(withText("foo-bar")))
    }

    @Test
    fun testLoaded() {
        val repo = TestUtil.createRepo("foo", "bar", "buzz")
        repoLiveData.postValue(Resource.success(repo))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.name)).check(
            matches(
                withText(getString(R.string.repo_full_name, "foo", "bar"))
            )
        )
        onView(withId(R.id.description)).check(matches(withText("buzz")))
    }

    @Test
    @Ignore("https://github.com/android/architecture-components-samples/issues/1076")
    fun testError() {
        repoLiveData.postValue(Resource.error("foo", null))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.retry)).check(matches(isDisplayed()))
        onView(withId(R.id.retry)).perform(click())
        verify(viewModel).retry()
        repoLiveData.postValue(Resource.loading(null))

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())))
        val repo = TestUtil.createRepo("owner", "name", "desc")
        repoLiveData.postValue(Resource.success(repo))

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())))
        onView(withId(R.id.name)).check(
            matches(
                withText(getString(R.string.repo_full_name, "owner", "name"))
            )
        )
        onView(withId(R.id.description)).check(matches(withText("desc")))
    }

    @Test
    fun testContributors() {
        setContributors("aa", "bb")
        onView(listMatcher().atPosition(0))
            .check(matches(hasDescendant(withText("aa"))))
        onView(listMatcher().atPosition(1))
            .check(matches(hasDescendant(withText("bb"))))
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.contributor_list)
    }

    @Test
    @Ignore("https://github.com/android/architecture-components-samples/issues/1076")
    fun testContributorClick() {
        setContributors("aa", "bb", "cc")
        onView(withText("cc")).perform(click())
        verify(navController).navigate(
            eq(RepoFragmentDirections.showUser("cc")),
            any(FragmentNavigator.Extras::class.java)
        )
    }

    @Test
    fun nullRepo() {
        repoLiveData.postValue(null)
        onView(withId(R.id.name)).check(matches(not(isDisplayed())))
    }

    @Test
    fun nullContributors() {
        setContributors("a", "b", "c")
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("a"))))
        contributorsLiveData.postValue(null)
        onView(listMatcher().atPosition(0)).check(doesNotExist())
    }

    private fun setContributors(vararg names: String) {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        val contributors = names.mapIndexed { index, name ->
            TestUtil.createContributor(
                repo = repo,
                login = name,
                contributions = 100 - index
            )
        }
        contributorsLiveData.postValue(Resource.success(contributors))
    }

    private fun getString(@StringRes id: Int, vararg args: Any): String {
        return ApplicationProvider.getApplicationContext<Context>().getString(id, *args)
    }
}
