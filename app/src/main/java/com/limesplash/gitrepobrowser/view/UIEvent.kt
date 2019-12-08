package com.limesplash.gitrepobrowser.view

sealed class UIEvent {
    data class UISerchRepoEvent(val query: String, val topic: String? = null, val lang: String?= null):UIEvent()
}