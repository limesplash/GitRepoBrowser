package com.limesplash.gitrepobrowser.presenter

import com.limesplash.gitrepobrowser.api.GithubApi
import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.model.SearchResult
import io.reactivex.Observable
import javax.inject.Inject

class ApiSearchReposUseCase @Inject constructor(var githubApi: GithubApi) : SearchReposUseCase {

    override fun searchRepos(searchQuery: SearchQuery) : Observable<SearchResult> =
        githubApi.searchRepositories(searchQuery.toQueryString())
}