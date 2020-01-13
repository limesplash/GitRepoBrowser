package com.limesplash.gitrepobrowser.view

import android.os.AsyncTask
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.limesplash.gitrepobrowser.R
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityInstrumentedTest{

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    private val activity: MainActivity
    get() = activityRule.activity

    @Before
    fun setUp() {
        val scheduler = Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
    }

    @Test
    fun querySearch() {

        onView(withId(R.id.search_text))
            .perform(ViewActions.typeText("Robolectric"))

        Thread.sleep(5000)

        assertEquals("robolectric - Android Unit Testing Framework", (activity.repos_list.getChildAt(0) as TextView).text)


    }

    @Test
    fun querySearchFilterLang() {

        onView(withId(R.id.search_text))
            .perform(ViewActions.typeText("liquicouch"))
        onView(withId(R.id.show_filters)).perform(ViewActions.click())

        onView(withId(R.id.lang_text))
            .perform(ViewActions.typeText("kotlin"))

        onView(withId(R.id.apply_filters)).perform(ViewActions.click())

        Thread.sleep(5000)

        assertEquals("liquicouch - LiquiCouch is a data migration tool for Java, Scala, Kotlin (or any other JVM lang) inspired by Liquibase",
            (activity.repos_list.getChildAt(0) as TextView).text )


    }
}