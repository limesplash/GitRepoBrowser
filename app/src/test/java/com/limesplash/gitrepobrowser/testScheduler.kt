package com.limesplash.gitrepobrowser

import io.reactivex.schedulers.TestScheduler

/**
 * so RxJavaPlugins.setIoSchedulerHandler obviously keeps a static reference to the first scheduler it has been initialized
 * so we keep a static reference to the scheduler too so that we can use it
 */

var testScheduler: TestScheduler = TestScheduler()