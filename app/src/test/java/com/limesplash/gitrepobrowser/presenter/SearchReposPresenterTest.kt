package com.limesplash.gitrepobrowser.presenter

import com.limesplash.gitrepobrowser.AbsRetrofitTest
import com.limesplash.gitrepobrowser.model.GitReposViewState
import com.limesplash.gitrepobrowser.model.GithubRepo
import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.model.SearchResult
import com.limesplash.gitrepobrowser.view.GitReposView
import com.limesplash.gitrepobrowser.view.UIEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class SearchReposPresenterTest: AbsRetrofitTest() {

    lateinit var presenter: SearchReposPresenter
    lateinit var gitReposView: GitReposView
    lateinit var searchReposUseCase: SearchReposUseCase

    @Before
    override fun init() {
        super.init()
        searchReposUseCase = mockk()

        every { searchReposUseCase.searchRepos(any()) } returns Observable.just(SearchResult())

        presenter = SearchReposPresenter(searchReposUseCase)

        gitReposView = mockk()

    }

    @Test
    fun bindIntents() {
        every { gitReposView.emitUserInput() }.returns(Observable.just(UIEvent.UISerchRepoEvent("")))

        //this is where bindIntents gets called in mosby
        presenter.attachView(gitReposView)

        verify { gitReposView.emitUserInput() }
    }

    @Test
    fun emptyUiEvent_noSearch() {
        every { gitReposView.emitUserInput() }.returns(Observable.just(UIEvent.UISerchRepoEvent("") as UIEvent).concatWith(Observable.never()))

        presenter.attachView(gitReposView)

        scheduler.advanceTimeBy(DEBOUNCE_TIME_MILLS, TimeUnit.MILLISECONDS)

        verify(exactly = 0)  { searchReposUseCase.searchRepos(any()) }
    }

    @Test
    fun uiEvent_callSearch() {

        every { gitReposView.emitUserInput() }.returns(Observable.just(UIEvent.UISerchRepoEvent("testQuery", "testTopic", "kotlin")))

        presenter.attachView(gitReposView)

        scheduler.advanceTimeBy(DEBOUNCE_TIME_MILLS, TimeUnit.MILLISECONDS)

        verify { searchReposUseCase.searchRepos(SearchQuery("testQuery", "testTopic", "kotlin")) }
    }

    @Test
    fun uiEvent_updatesUi() {
        every { gitReposView.emitUserInput() }.returns(Observable.just(UIEvent.UISerchRepoEvent("")))

        presenter.attachView(gitReposView)
//
        scheduler.advanceTimeBy(DEBOUNCE_TIME_MILLS, TimeUnit.MILLISECONDS)

        verify {
            gitReposView.updateUi(GitReposViewState.ResultsState(SearchResult()))
        }

    }

    @Test
    fun uiEventSimpleQuery_updatesUi() {

        val uiObservable: Observable<UIEvent> = Observable.just(UIEvent.UISerchRepoEvent("testQuery") as UIEvent).concatWith(Observable.never())

//      there seems to be an issue with a mokkk-ed view due to the peculiar way mosby uses rxjava

//        every { gitReposView.emitUserInput() }.returns(uiObservable)

//      so the view is mocked manually

        var actualStates:ArrayList<GitReposViewState> = arrayListOf()

        gitReposView = object:GitReposView {

            override fun emitUserInput(): Observable<UIEvent> = uiObservable

            override fun updateUi(state: GitReposViewState) { actualStates.add(state) }
        }

        every { searchReposUseCase.searchRepos(any()) } returns
                Observable.just(
                    SearchResult(1, listOf(GithubRepo("testName", "testFullName", "testDescr")))
                )
                .concatWith(Observable.never())

        presenter.attachView(gitReposView)

        scheduler.advanceTimeBy(DEBOUNCE_TIME_MILLS, TimeUnit.MILLISECONDS)

        val expectedStates =  listOf(
            GitReposViewState.Loading(),
            GitReposViewState.ResultsState(SearchResult(1,listOf(GithubRepo("testName", "testFullName", "testDescr")))))

        Assert.assertEquals(
            expectedStates,
            actualStates
        )

//        verify{
//            gitReposView.updateUi(GitReposViewState.Loading())
//
//            gitReposView.updateUi(GitReposViewState.ResultsState(
//                SearchResult(1,listOf(GithubRepo("testName", "testFullName", "testDescr")))
//                )
//            )
//        }

    }

}