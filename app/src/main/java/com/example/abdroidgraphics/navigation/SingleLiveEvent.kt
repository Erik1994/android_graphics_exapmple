package com.example.abdroidgraphics.navigation

class SingleLiveEvent<out T> (private val content: T) {

    private var hasBeenHandled: Boolean = false

    fun getContentIfNorHandled(): T? = if(hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    fun peekContent(): T = content
}