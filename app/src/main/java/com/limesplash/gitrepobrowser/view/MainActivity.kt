package com.limesplash.gitrepobrowser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import com.limesplash.gitrepobrowser.R
import com.limesplash.gitrepobrowser.model.GitReposViewState
import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.presenter.SearchReposPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: GitReposView,  MviActivity<GitReposView, SearchReposPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchQueryObservable = RxTextView.textChangeEvents(search_text)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { it != null }
            .map { it.text().toString() }
    }

    private lateinit var searchQueryObservable: Observable<String>

    //TODO
    override fun emitUserInput(): Observable<UIEvent> =
        searchQueryObservable.map { UIEvent.UISerchRepoEvent(it) }


    override fun updateUi(state: GitReposViewState) {
        repos_list.adapter = ReposAdapter(state.searchResult.repositories)
    }

    override fun createPresenter(): SearchReposPresenter {
        //TODO inject
        return SearchReposPresenter()
    }
}
