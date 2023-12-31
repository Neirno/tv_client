package com.neirno.tv_client.navigation

sealed class Screen(val route: String) {
    object Main : Screen(NavigationRoutes.MAIN_SCREEN)
    object History : Screen(NavigationRoutes.HISTORY_SCREEN)
    object Connection : Screen(NavigationRoutes.CONNECTION_SCREEN)
  }
