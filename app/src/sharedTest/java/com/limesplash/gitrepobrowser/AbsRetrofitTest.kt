package com.limesplash.gitrepobrowser

import androidx.annotation.CallSuper
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before

abstract class AbsRetrofitTest {

    companion object {

        const val DEBOUNCE_TIME_MILLS = 500L
        private val s_testScheduler = TestScheduler()
    }

    protected val scheduler: TestScheduler
        get() = s_testScheduler

    @Before
    @CallSuper
    open fun init() {
        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setErrorHandler {  } //hide logs
        scheduler.start()
    }

    @After
    @CallSuper
    open fun done() {
        scheduler.shutdown()
    }




}