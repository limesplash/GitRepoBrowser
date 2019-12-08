package com.limesplash.gitrepobrowser.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("total_count") val totalCount: Int = 0,
    @SerializedName("items") val repositories: List<GithubRepo> = emptyList()
)