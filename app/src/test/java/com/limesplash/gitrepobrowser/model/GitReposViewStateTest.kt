package com.limesplash.gitrepobrowser.model

import org.junit.Assert.*
import org.junit.Test

class GitReposViewStateTest {

    @Test
    fun loading_equals() {
        val loading = GitReposViewState.Loading()

        assertEquals(loading, GitReposViewState.Loading())

        assertNotEquals(loading, GitReposViewState.ResultsState())
    }
}