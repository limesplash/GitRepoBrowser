package com.limesplash.gitrepobrowser

import com.limesplash.gitrepobrowser.api.GithubApi
import com.limesplash.gitrepobrowser.presenter.ApiSearchReposUseCase
import com.limesplash.gitrepobrowser.presenter.SearchReposPresenter
import com.limesplash.gitrepobrowser.presenter.SearchReposUseCase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class GitReposModule{

    @Singleton
    @Provides
    fun okHTTPClient(): OkHttpClient {
        val client = OkHttpClient.Builder()

        client.connectTimeout(10, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)

//        client.interceptors().add(object : Interceptor {
//            @Throws(IOException::class)
//            override fun intercept(chain: Interceptor.Chain?): Response? {
//                try {
//                    return chain!!.proceed(chain.request())
//                } catch (exception: SocketTimeoutException) {
//                    exception.printStackTrace()
//                } catch (exception: InterruptedIOException) {
//                    exception.printStackTrace()
//                }
//                return chain!!.proceed(chain.request())
//            }
//        })
        return client.build()
    }

    @Singleton
    @Provides
    fun retrofit(okHTTP: OkHttpClient): Retrofit = retrofit(okHTTP, BuildConfig.GITHUB_HOST_URL)

    /*
        expose for testing
     */
    fun retrofit(okHTTP: OkHttpClient, baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHTTP)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Singleton
    @Provides
    fun githubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

    @Provides
    fun presenterSchedulers(): SearchReposPresenter.SchedulerProvider = SearchReposPresenter.DefaultSchedulerProvider()

    @Provides
    fun presenterApiUseCase(api :GithubApi): SearchReposUseCase = ApiSearchReposUseCase(api)


}