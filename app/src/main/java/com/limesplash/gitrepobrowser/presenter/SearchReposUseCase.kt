package com.limesplash.gitrepobrowser.presenter

import com.limesplash.gitrepobrowser.api.GithubApi
import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.model.SearchResult
import io.reactivex.Observable
import javax.inject.Inject

class SearchReposUseCase @Inject constructor(var githubApi: GithubApi) {

    fun searchRepos(searchQuery: SearchQuery) : Observable<SearchResult> {
        return githubApi.searchRepositories(searchQuery.toQueryString())
    }
}