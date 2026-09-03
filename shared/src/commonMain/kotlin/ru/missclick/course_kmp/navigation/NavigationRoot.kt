package ru.missclick.course_kmp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.missclick.auth.presentation.navigation.AuthGraphRoutes
import ru.missclick.auth.presentation.navigation.authGraph
import ru.missclick.chat.presentation.navigation.ChatGraphRoute
import ru.missclick.chat.presentation.navigation.chatGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(ChatGraphRoute.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )

        chatGraph(
            navController = navController,
            onLogout = {
                navController.navigate(AuthGraphRoutes.Graph) {
                    popUpTo<ChatGraphRoute.Graph>() {
                        inclusive = true
                    }
                }
            }
        )
    }
}