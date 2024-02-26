package com.loc.newsapp.presentation.news_from_source

import androidx.paging.PagingData
import com.loc.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

data class NewsFromSourceState(
    val url: String = "",
    val articles: Flow<PagingData<Article>>? = null
)