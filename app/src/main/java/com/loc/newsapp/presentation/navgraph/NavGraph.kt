package com.loc.newsapp.presentation.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.newsapp.presentation.bookmark.BookmarkScreen
import com.loc.newsapp.presentation.bookmark.BookmarkViewModel
import com.loc.newsapp.presentation.home.HomeScreen
import com.loc.newsapp.presentation.home.HomeViewModel
import com.loc.newsapp.presentation.news_navigator.NewsNavigator
import com.loc.newsapp.presentation.onboarding.OnBoardingScreen
import com.loc.newsapp.presentation.onboarding.OnBoardingViewModel
import com.loc.newsapp.presentation.search.SearchScreen
import com.loc.newsapp.presentation.search.SearchViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController();

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route,
        ) {
            composable(
                route = Route.OnBoardingScreen.route
            ) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(viewModel::onEvent)
            }
        }

        navigation(
            route = Route.NewsNavigation.route,
            startDestination = Route.NewsNavigatorScreen.route
        ) {
            composable(
                route = Route.NewsNavigatorScreen.route
            ) {
//                val viewModel: HomeViewModel = hiltViewModel()
//                val articles = viewModel.news.collectAsLazyPagingItems();
//                HomeScreen(articles = articles, navigate = {})

//                val viewModel: SearchViewModel = hiltViewModel()
//                SearchScreen(state = viewModel.state.value, event = viewModel::onEvent, navigate = {})

//                val viewModel: BookmarkViewModel = hiltViewModel()
//                BookmarkScreen(state = viewModel.state.value, navigate = {})
                NewsNavigator()
            }
        }
    }
}