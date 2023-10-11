package com.neirno.tv_client.navigation

import androidx.navigation.NavController

class NavigationManager(private val navController: NavController) : Navigator {
    override fun toMainScreen() {
        navController.navigate(NavigationRoutes.MAIN_SCREEN) {
            popUpTo(NavigationRoutes.CONNECTION_SCREEN) { inclusive = true }
        }
    }

    override fun toHistoryScreen() {
        navController.navigate(NavigationRoutes.HISTORY_SCREEN)
    }

    override fun toConnectionScreen() {
        navController.navigate(NavigationRoutes.CONNECTION_SCREEN)
    }

    override fun back() {
        navController.popBackStack()
    }
}

