package com.limesplash.gitrepobrowser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.limesplash.gitrepobrowser.DaggerGitReposComponent
import com.limesplash.gitrepobrowser.R
import com.limesplash.gitrepobrowser.model.GitReposViewState
import com.limesplash.gitrepobrowser.model.SearchQuery
import com.limesplash.gitrepobrowser.presenter.SearchReposPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity: GitReposView,  MviActivity<GitReposView, SearchReposPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

        searchQueryObservable = RxTextView.textChangeEvents(search_text)
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(200, TimeUnit.MILLISECONDS)
            .filter { it != null }
            .map { it.text().toString() }

        applyFiltersObservable = RxView.clicks(apply_filters)

        show_filters.setOnClickListener { toggleFilters() }

//        filters_container.visibility = View.GONE


    }

    private lateinit var searchQueryObservable: Observable<String>

    private lateinit var applyFiltersObservable :Observable<Any?>

    override fun emitUserInput(): Observable<UIEvent> = Observable.merge (
            searchQueryObservable.map { UIEvent.UISerchRepoEvent(it, topic_text.text.toString(), lang_text.text.toString()) },
            applyFiltersObservable.map { UIEvent.UISerchRepoEvent(search_text.text.toString(), topic_text.text.toString(), lang_text.text.toString()) }
        )


    override fun updateUi(state: GitReposViewState) {
        when(state) {
            is GitReposViewState.Loading ->  result_count.text = "Loading ..."
            is GitReposViewState.ResultsState -> {
                repos_list.adapter = ReposAdapter(state.searchResult.repositories)
                val loadedCount = state.searchResult.repositories.size
                result_count.text =  loadedCount.toString() +" of "+ state.searchResult.totalCount.toString()+" Hits"
            }
        }

    }


    override fun createPresenter(): SearchReposPresenter = DaggerGitReposComponent.builder().build().presenter()

    //this is pure view logic and for quicker implementation it does not get updated by state
    //so it is not persisted
    private fun toggleFilters() {
        filters_container.visibility = if( filters_container.visibility == View.GONE ) {
            View.VISIBLE
        }else {
            View.GONE
        }
    }
}
