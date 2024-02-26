package com.loc.newsapp.data.repository

import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.loc.newsapp.data.local.NewsDao
import com.loc.newsapp.data.remote.NewsApi
import com.loc.newsapp.data.remote.NewsFromUrlPagingSource
import com.loc.newsapp.data.remote.NewsPagingSource
import com.loc.newsapp.data.remote.SearchNewsPagingSource
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class NewsRepositoryImpl (
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
): NewsRepository {

    override fun getNewsFromUrl(url: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = {
                NewsFromUrlPagingSource(
                    newsApi = newsApi,
                    url = url
                )
            }
        ).flow
    }
    override fun getNews(sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                NewsPagingSource(
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ",")
                )
            }
        ).flow
    }

    override fun searchNews(searchQuery: String, sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    newsApi = newsApi,
                    sources = sources.joinToString(separator = ","),
                    searchQuery = searchQuery
                )
            }
        ).flow
    }

    override suspend fun upsertArticle(article: Article) {
        newsDao.upsert(article)
    }

    override suspend fun deleteArticle(article: Article) {
        newsDao.delete(article)
    }

    override fun selectArticles(): Flow<List<Article>> {
        return newsDao.getArticles().onEach { it.reversed() }
    }

    override suspend fun selectArticle(url: String): Article? {
        return newsDao.getArticle(url)
    }


}