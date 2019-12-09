package com.limesplash.gitrepobrowser.api

import com.limesplash.gitrepobrowser.model.SearchResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("/search/repositories")
    fun searchRepositories(@Query("q") query: String,
                           @Query("sort") sort: String = "stars",
                           @Query("order") order: String = "desc",
                           @Query("per_page") perPage: Int = 100,
                           @Query("page") page: Int = 1): Observable<SearchResult>
}