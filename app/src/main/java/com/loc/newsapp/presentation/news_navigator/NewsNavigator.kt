package com.loc.newsapp.presentation.news_navigator

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.presentation.details.DetailsEvent
import com.loc.newsapp.presentation.details.DetailsScreen
import com.loc.newsapp.presentation.details.DetailsViewModel
import com.loc.newsapp.presentation.navgraph.Route
import com.loc.newsapp.presentation.news_from_source.NewsFromSourceScreen
import com.loc.newsapp.presentation.news_from_source.NewsFromSourceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsNavigator() {
    val navController = rememberNavController();
    val backstackState = navController.currentBackStackEntryAsState().value;
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    selectedItem = remember(key1 = backstackState) {
        when (backstackState?.destination?.route) {
            Route.SearchScreen.route -> 0
            else -> 0
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            modifier = Modifier.padding(bottom = bottomPadding),
            navController = navController,
            startDestination = Route.SearchScreen.route
        ) {

            composable(route = Route.SearchScreen.route) {
                val viewModel: NewsFromSourceViewModel = hiltViewModel();
                val state = viewModel.state.value
                NewsFromSourceScreen(
                    state = state,
                    event = viewModel::onEvent,
                    navigateToDetails = { article ->
                        navigateToDetails(
                            navController = navController,
                            article = article
                        )
                    }
                )
            }

            composable(route = Route.DetailsScreen.route) {
                val viewModel: DetailsViewModel = hiltViewModel();
                if (viewModel.sideEffect != null) {
                    Toast.makeText(LocalContext.current, viewModel.sideEffect, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.onEvent(DetailsEvent.RemoveSideEffect)
                }
                navController.previousBackStackEntry?.savedStateHandle?.get<Article?>("article")
                    ?.let { article ->
                        DetailsScreen(
                            article = article,
                            event = viewModel::onEvent,
                            navigateUp = { navController.navigateUp() })
                    }
            }
        }
    }

}

private fun navigateToDetails(navController: NavController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
    navController.navigate(Route.DetailsScreen.route);
}