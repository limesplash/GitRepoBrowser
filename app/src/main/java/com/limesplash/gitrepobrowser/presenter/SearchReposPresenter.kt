package com.limesplash.gitrepobrowser.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.limesplash.gitrepobrowser.model.GitReposViewState
import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.model.SearchResult
import com.limesplash.gitrepobrowser.view.GitReposView
import com.limesplash.gitrepobrowser.view.UIEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchReposPresenter: MviBasePresenter<GitReposView, GitReposViewState>() {

    override fun bindIntents() {
        val userInput = intent(GitReposView::emitUserInput)
            .subscribeOn(Schedulers.io())
            .debounce(500, TimeUnit.MILLISECONDS)
            .flatMap {
                when(it) {
                    is UIEvent.UISerchRepoEvent -> {
                        if(it.query.isNotEmpty())
                            SearchReposUseCase.searchRepos(SearchQuery(it.query, it.topic, it.lang))
                                .map { GitReposViewState(it) }
                        else
                            Observable.just(GitReposViewState(SearchResult()))

                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())


        //TODO initial state

        subscribeViewState(userInput,GitReposView::updateUi)

    }
}