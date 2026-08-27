package ru.missclick.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.missclick.chat.presentation.chatListDetail.ChatListDetailAdaptiveLayout

sealed interface ChatGraphRoute {
    @Serializable
    data object Graph

    @Serializable
    data object ChatListDetailRoute: ChatGraphRoute

}

fun NavGraphBuilder.chatGraph(
    navController: NavController
) {
    navigation<ChatGraphRoute.Graph>(
        startDestination = ChatGraphRoute.ChatListDetailRoute
    ) {
        composable<ChatGraphRoute.ChatListDetailRoute> {
            ChatListDetailAdaptiveLayout()
        }
    }
}