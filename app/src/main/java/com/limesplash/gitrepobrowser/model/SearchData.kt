package com.limesplash.gitrepobrowser.model

import com.google.gson.annotations.SerializedName

data class SearchData(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") val repositories: List<GithubRepo>
)