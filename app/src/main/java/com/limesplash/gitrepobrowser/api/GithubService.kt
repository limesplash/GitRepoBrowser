package com.limesplash.gitrepobrowser.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


class GithubService {
    companion object {
        val URL = "https://api.github.com"

        fun retrofit(): Retrofit {
            val client = OkHttpClient.Builder()

            client.connectTimeout(10, TimeUnit.SECONDS)
            client.readTimeout(30, TimeUnit.SECONDS)

//            client.interceptors().add(object : Interceptor {
//                @Throws(IOException::class)
//                override fun intercept(chain: Interceptor.Chain?): Response? {
//                    try {
//                        return chain!!.proceed(chain.request())
//                    } catch (exception: SocketTimeoutException) {
//                        exception.printStackTrace()
//                    } catch (exception: InterruptedIOException) {
//                        exception.printStackTrace()
//                    }
//                    return null
//                    //chain!!.proceed(chain.request())
//                }
//            })
            return Retrofit.Builder()
                .baseUrl(URL)
                .client(client.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}