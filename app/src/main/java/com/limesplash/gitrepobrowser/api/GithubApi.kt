package com.limesplash.gitrepobrowser.api

import com.limesplash.gitrepobrowser.model.SearchData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("/search/repositories")
    fun searchRepositories(@Query("q") query: String,
                           @Query("sort") sort:String = "stars",
                           @Query("order") order:String = "desc"): Observable<SearchData>
}