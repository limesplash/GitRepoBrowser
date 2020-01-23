package com.limesplash.gitrepobrowser.view

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.limesplash.gitrepobrowser.AbsRetrofitTest
import com.limesplash.gitrepobrowser.R
import com.limesplash.gitrepobrowser.model.GitReposViewState
import com.limesplash.gitrepobrowser.model.GithubRepo
import com.limesplash.gitrepobrowser.model.SearchResult
import io.reactivex.observers.TestObserver
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest:AbsRetrofitTest() {

    lateinit var activityScenario: ActivityScenario<MainActivity>


    @Before
    override fun init() {
        super.init()
        activityScenario = launch(MainActivity::class.java)

    }

    @After
    override fun done() {
        super.done()
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