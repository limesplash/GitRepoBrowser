package com.limesplash.gitrepobrowser

import com.limesplash.gitrepobrowser.api.GithubApi
import com.limesplash.gitrepobrowser.api.GithubService
import com.limesplash.gitrepobrowser.presenter.SearchReposPresenter
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
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
    fun retrofit(okHTTP: OkHttpClient): Retrofit = Retrofit.Builder()
                .baseUrl(GithubService.URL)
                .client(okHTTP)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()




    @Singleton
    @Provides
    fun githubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

}