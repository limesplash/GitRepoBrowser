package com.limesplash.gitrepobrowser

import com.limesplash.gitrepobrowser.api.GithubApi
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class GitReposModuleTest {

    val module: GitReposModule
        get() = GitReposModule()

    @Test
    fun okHTTPClient() {
        assertNotNull(module.okHTTPClient())
    }

    @Test
    fun retrofit() {
        assertNotNull(module.retrofit(module.okHTTPClient()))
    }

    @Test
    fun githubApi() {
        val api: GithubApi = apiFromModule()
        assertNotNull(api)
    }

    fun apiFromModule() = module.githubApi(
        module.retrofit(
        module.okHTTPClient()
        )
    )



    @Test
    fun presenterSchedulers() {
    }

    @Test
    fun presenterApiUseCase() {
    }
}