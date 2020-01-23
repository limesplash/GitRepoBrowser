package com.limesplash.gitrepobrowser.api

import androidx.test.runner.AndroidJUnit4
import com.limesplash.gitrepobrowser.GitReposModule
import com.limesplash.gitrepobrowser.model.GithubRepo
import com.limesplash.gitrepobrowser.model.SearchResult
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(PowerMockRunner::class)
//@PowerMockIgnore(
//    "io.mockk.proxy.*"
//)
//@RunWith(AndroidJUnit4::class)
class GithubApiTest {
//
//    @JvmField
//    @Rule
//    var rule = PowerMockRule()


    private var mockWebServer = MockWebServer()

    private lateinit var api: GithubApi

    @Before
    fun init() {

        mockWebServer.start(8080)

        with(GitReposModule()) {
            api = retrofit(okHTTPClient(),mockWebServer.url("/").toString()).create(GithubApi::class.java)
        }

    }

    @After
    fun done() {
        mockWebServer.shutdown()
    }

    @Test()
    fun testApiResponseCall() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("""
            {
              "total_count": 1,
              "items": [
                    {
                      "name": "robolectric",
                      "full_name": "robolectric/robolectric"
                    }
                ]
            }
        """.trimIndent()))

        val testObserver = TestObserver<SearchResult>()
        api.searchRepositories("dummyQuery").subscribe(testObserver)

        testObserver.assertValue(SearchResult(1, listOf(GithubRepo("robolectric", "robolectric/robolectric"))))
    }

    @Test
    fun testApiRequest() {
        var recordedRequest: RecordedRequest? = null

        mockWebServer.dispatcher = object:Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                recordedRequest = request
                return MockResponse().setResponseCode(200).setBody("{ total_count: 0, items: [] }")
            }
        }

        api.searchRepositories("testQuery", "stars", "desc", 100, 1).subscribe()

        assertEquals("/search/repositories?q=testQuery&sort=stars&order=desc&per_page=100&page=1",recordedRequest?.path)
    }
}