package com.limesplash.gitrepobrowser.presenter

import com.limesplash.gitrepobrowser.api.GithubApi
import com.limesplash.gitrepobrowser.api.GithubService
import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.model.SearchResult
import io.reactivex.Observable

object SearchReposUseCase {

    val githubApi = null
    fun searchRepos(searchQuery: SearchQuery) : Observable<SearchResult> {
        return GithubService.retrofit().create(GithubApi::class.java).searchRepositories(searchQuery.toQueryString())
    }
}