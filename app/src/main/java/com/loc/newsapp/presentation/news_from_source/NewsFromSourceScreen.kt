package com.loc.newsapp.presentation.news_from_source

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.presentation.Dimens
import com.loc.newsapp.presentation.common.ArticlesList
import com.loc.newsapp.presentation.common.SearchBar

@Composable
fun NewsFromSourceScreen(
    state: NewsFromSourceState,
    event: (NewsFromSourceEvent) -> Unit,
    navigateToDetails: (article: Article) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(top = Dimens.MediumPadding1, start = Dimens.MediumPadding1, end = Dimens.MediumPadding1)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        SearchBar(
            text = state.url,
            readOnly = false,
            onValueChange = { event(NewsFromSourceEvent.UpdateSourceUrl(it)) },
            onSearch = { event(NewsFromSourceEvent.GetNewsFromSource) }
        )
        Spacer(modifier = Modifier.padding(Dimens.MediumPadding1))
        state.articles?.let {
            val articles = it.collectAsLazyPagingItems()
            ArticlesList(articles = articles, onClick = {
                navigateToDetails(it)
            })
        }
    }
}