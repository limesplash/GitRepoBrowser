package com.limesplash.gitrepobrowser.model

import com.google.gson.annotations.SerializedName

data class GithubRepo(val name: String,
                      @SerializedName("full_name") val fullName: String,
                      val description: String?) {
    override fun toString(): String {
        return name + ( if(description != null && description.isNotBlank() ) { " - $description"}else{ "" })
    }
}