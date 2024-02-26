package com.loc.newsapp.presentation.news_from_source

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsFromSourceViewModel @Inject constructor(
    private var newsUseCases: NewsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(NewsFromSourceState())
    val state: State<NewsFromSourceState> = _state

    fun onEvent(event: NewsFromSourceEvent) {
        when(event) {
            is NewsFromSourceEvent.UpdateSourceUrl -> {
                _state.value = state.value.copy(url = event.url)
            }
            is NewsFromSourceEvent.GetNewsFromSource -> {
                getNewsFromSource(_state.value.url)
            }
        }
    }

    private fun getNewsFromSource(url: String) {
        val articles = newsUseCases.getNewsFromUrl(
            url = url
        ).cachedIn(viewModelScope)
        _state.value = state.value.copy(articles = articles)
    }
}