package com.limesplash.gitrepobrowser.model

sealed class GitReposViewState {
    data class ResultsState( val searchResult: SearchResult = SearchResult()): GitReposViewState()
    class Loading: GitReposViewState() {
        //for testing purposes
        override fun equals(other: Any?): Boolean = other!= null && other is Loading
    }
}
