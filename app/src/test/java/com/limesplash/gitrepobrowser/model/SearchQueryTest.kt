package com.limesplash.gitrepobrowser.model

import org.junit.Test

import org.junit.Assert.*

class SearchQueryTest {

    @Test
    fun toQueryString_query() {
        val searchQuery = SearchQuery(TestData.query, null, null)
        assertEquals(
            "${TestData.query}",
                    searchQuery.toQueryString())
    }

    @Test
    fun toQueryString_allParams() {

        val searchQuery = SearchQuery(TestData.query,TestData.topic,TestData.lang)

        assertEquals(
            "${TestData.query}+topic:${TestData.topic}+lang:${TestData.lang}",
            searchQuery.toQueryString())
    }
}

object TestData {
    const val query = "testQuery"
    const val topic = "testTopic"
    const val lang = "en"
}