package com.loc.newsapp.presentation.news_from_source

sealed class NewsFromSourceEvent {
    data class UpdateSourceUrl(val url: String): NewsFromSourceEvent()

    object GetNewsFromSource : NewsFromSourceEvent()
}