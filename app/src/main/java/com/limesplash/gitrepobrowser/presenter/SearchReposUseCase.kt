package com.limesplash.gitrepobrowser.presenter

import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.model.SearchResult
import io.reactivex.Observable

interface SearchReposUseCase {
    fun searchRepos(searchQuery: SearchQuery) : Observable<SearchResult>
}