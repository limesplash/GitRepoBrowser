package com.limesplash.gitrepobrowser.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.limesplash.gitrepobrowser.model.GitReposViewState
import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.model.SearchResult
import com.limesplash.gitrepobrowser.view.GitReposView
import com.limesplash.gitrepobrowser.view.UIEvent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchReposPresenter @Inject constructor(private val searchReposUseCase: SearchReposUseCase,
                                               private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider()):
                                                        MviBasePresenter<GitReposView, GitReposViewState>() {

    interface SchedulerProvider {

        fun provideForegroundScheduler(): Scheduler

        fun provideBackgroundScheduler(): Scheduler
    }

    class DefaultSchedulerProvider: SchedulerProvider {

        override fun provideForegroundScheduler(): Scheduler = AndroidSchedulers.mainThread()

        override fun provideBackgroundScheduler(): Scheduler = Schedulers.io()
    }


    override fun bindIntents() {

        val foregroundScheduler = schedulerProvider.provideForegroundScheduler()
        val bgrScheduler = schedulerProvider.provideBackgroundScheduler()

        val userInput = intent(GitReposView::emitUserInput)
            .subscribeOn(bgrScheduler)
            .debounce(500, TimeUnit.MILLISECONDS, bgrScheduler)
            .flatMap {
                when(it) {
                    is UIEvent.UISerchRepoEvent -> {
                        if(it.query.isNotEmpty())
                            searchReposUseCase.searchRepos( SearchQuery(it.query, it.topic, it.lang) )
                                .map { GitReposViewState.ResultsState(it) as GitReposViewState }
                                .doOnError { it.printStackTrace() } //TODO err
                                .startWith( GitReposViewState.Loading() )
                        else
                            Observable.just( GitReposViewState.ResultsState( SearchResult() ) )

                    }
                }
            }
            .doOnError {
                //Nothing ?
                //it.printStackTrace()
            }

//            .onErrorReturnItem(GitReposViewState.Loading()) //TODO
            .observeOn(foregroundScheduler)


        //TODO initial state

        subscribeViewState(userInput,GitReposView::updateUi)

    }
}