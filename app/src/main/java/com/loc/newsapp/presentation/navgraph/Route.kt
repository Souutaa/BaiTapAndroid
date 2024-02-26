package com.loc.newsapp.presentation.navgraph

sealed class Route(
    val route: String
) {
    object OnBoardingScreen: Route(route = "onBoardingScreen")
//    object HomeScreen: Route(route = "homeScreen")
    object SearchScreen: Route(route = "searchScreen")
//    object BookMarkScreen: Route(route = "bookMarkScreen")
    object DetailsScreen: Route(route = "DetailsScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object NewsNavigation : Route(route = "newsNavigation")
    object NewsNavigatorScreen : Route(route = "newsNavigatorScreen")
}