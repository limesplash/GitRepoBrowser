package com.limesplash.gitrepobrowser

import com.limesplash.gitrepobrowser.presenter.SearchReposPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GitReposModule::class])
interface GitReposComponent {
    fun presenter(): SearchReposPresenter
}