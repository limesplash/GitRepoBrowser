package com.limesplash.gitrepobrowser.view

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.limesplash.gitrepobrowser.model.GitReposViewState
import io.reactivex.Observable

interface GitReposView:MvpView {
    fun emitUserInput(): Observable<UIEvent>
    fun updateUi(state :GitReposViewState)
}