package ru.missclick.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.missclick.chat.presentation.chatListDetail.ChatListDetailAdaptiveLayout

sealed interface ChatGraphRoute {
    @Serializable
    data object Graph

    @Serializable
    data class ChatListDetailRoute(val chatId: String? = null): ChatGraphRoute

}

fun NavGraphBuilder.chatGraph(
    navController: NavController
) {
    navigation<ChatGraphRoute.Graph>(
        startDestination = ChatGraphRoute.ChatListDetailRoute(null)
    ) {
        composable<ChatGraphRoute.ChatListDetailRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "course://chat_detail/{chatId}"
                }
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ChatGraphRoute.ChatListDetailRoute>()
            ChatListDetailAdaptiveLayout(
                initialChatId = route.chatId,
                onLogout = {}
            )
        }
    }
}