package com.limesplash.gitrepobrowser.model

data class SearchQuery(val query: String, val topic: String? = null, val lang: String? = null) {

    fun toQueryString(): String {
        var res = query

        var addParam: ( String, String) -> Unit = { paramName: String, paramValue: String ->
            res+="+$paramName:$paramValue"
        }
        if(!topic.isNullOrBlank())
            addParam("topic",topic)

        if(!lang.isNullOrBlank())
            addParam("lang",lang)


        return res
    }
}