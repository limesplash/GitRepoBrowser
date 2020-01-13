package com.limesplash.gitrepobrowser.view

import androidx.test.core.app.ActivityScenario.*
import android.text.Editable
import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.limesplash.gitrepobrowser.R
import com.limesplash.gitrepobrowser.model.GitReposViewState
import com.limesplash.gitrepobrowser.model.GithubRepo
import com.limesplash.gitrepobrowser.model.SearchResult
import com.limesplash.gitrepobrowser.testScheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    lateinit var activityScenario: ActivityScenario<MainActivity>

    var scheduler:TestScheduler = testScheduler

    @Before
    fun init() {

//        scheduler = TestScheduler()
        scheduler.start()
//        activityScenario = launch(MainActivity::class.java)
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }

        activityScenario = launch(MainActivity::class.java)

    }

    @After
    fun done() {
        scheduler.triggerActions()
        scheduler.shutdown()
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        activityScenario.close()
    }

    @Test
    fun toggleFilters() {
//
        var initialVisibility = false

        activityScenario.onActivity { initialVisibility = it.filters_container.isVisible }

        onView(withId(R.id.show_filters)).perform(click())
        onView(withId(R.id.filters_container)).check { view, _ -> assert(view.isVisible != initialVisibility) }

        onView(withId(R.id.show_filters)).perform(click())
        onView(withId(R.id.filters_container)).check { view, _ -> assert(view.isVisible == initialVisibility) }

    }

    @Test
    fun updateUi_initial() {
        activityScenario.onActivity { assertNotEquals("Loading ...", it.result_count.text) }

    }

    @Test
    fun updateUi_loading() {
        activityScenario.onActivity {
            it.updateUi(GitReposViewState.Loading())
            assertEquals("Loading ...", it.result_count.text)
        }
    }

    @Test
    fun updateUi_result() {
        activityScenario.onActivity {
            val repo =  GithubRepo("Robolectric","robolectric/robolectric")
            it.updateUi(GitReposViewState.ResultsState(SearchResult(1, listOf(repo))))
            assertEquals("1 of 1 Hits", it.result_count.text)
            assertEquals(repo.toString(), (it.repos_list.getChildAt(0) as TextView).text)
        }
    }

    @Test
    fun createPresenter() {
        activityScenario.onActivity {
            assertNotNull(it.createPresenter())
        }
    }

    @Test
    fun emitUserInput_applyFilters() {

        val observer: TestObserver<UIEvent> = TestObserver()

        activityScenario.onActivity {
            val userInput = it.emitUserInput()
            userInput.subscribe(observer)
        }


        onView(withId(R.id.search_text))
            .perform(typeText("Robolectric"))
        onView(withId(R.id.show_filters)).perform(click())

        onView(withId(R.id.lang_text)).perform(typeText("robo"))
        onView(withId(R.id.topic_text)).perform(typeText("topic"))


        onView(withId(R.id.apply_filters)).perform(click())

        scheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)
        scheduler.triggerActions()

        /**
         * since typing a query and tapping apply filters both emit ui Event
         */
        observer.assertValueAt(observer.valueCount() - 1,
            UIEvent.UISerchRepoEvent("Robolectric", "topic","robo")
        )

    }

    @Test
    fun emitUserInput_query() {


        val observer: TestObserver<UIEvent> = TestObserver()

        activityScenario.onActivity {
            val userInput = it.emitUserInput()
            userInput.subscribe(observer)
        }


        onView(withId(R.id.search_text))
            .perform(typeText("Robolectric2"))

        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS)

        scheduler.triggerActions()

        observer.assertValues(
            UIEvent.UISerchRepoEvent("Robolectric2", "",""))

    }


//
//    @Test
//    fun performSearch() {
//
//        onView(withId(R.id.search_text)).perform(typeText("Robolectric"))
//        onView(withId(R.id.lang_text)).perform(typeText("Java"))
//    }
}