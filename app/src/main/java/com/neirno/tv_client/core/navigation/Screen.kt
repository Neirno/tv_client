package com.neirno.tv_client.core.navigation

sealed class Screen(val route: String) {
    object Main : Screen(NavigationRoutes.MAIN_SCREEN)
    object History : Screen(NavigationRoutes.HISTORY_SCREEN)
    object Query : Screen(NavigationRoutes.QUERY_SCREEN)
    object Connection : Screen(NavigationRoutes.CONNECTION_SCREEN)
}
