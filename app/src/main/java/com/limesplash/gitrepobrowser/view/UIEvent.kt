package com.limesplash.gitrepobrowser.view

sealed class UIEvent {
    data class UISerchRepoEvent(val query: String, val topic: String?, val lang: String?):UIEvent()
}