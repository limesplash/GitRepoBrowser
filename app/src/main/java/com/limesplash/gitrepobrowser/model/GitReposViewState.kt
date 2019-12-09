package com.limesplash.gitrepobrowser.model

sealed class GitReposViewState {
    class ResultsState( val searchResult: SearchResult = SearchResult()): GitReposViewState()
    class Loading(): GitReposViewState()
}
