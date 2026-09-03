package ru.missclick.course_kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.auth.presentation.navigation.AuthGraphRoutes
import ru.missclick.chat.presentation.navigation.ChatGraphRoute
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.presentation.util.ObserveAsEvents
import ru.missclick.course_kmp.navigation.DeepLinkListener
import ru.missclick.course_kmp.navigation.NavigationRoot

@Composable
@Preview
fun App(
    onAuthenticationChecked: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCheckingAuth) {
        if (!state.isCheckingAuth) {
            onAuthenticationChecked()
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            MainEvent.OnSessionExpired -> {
                navController.navigate(AuthGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = false
                    }
                }
            }
        }
    }

    CourseTheme {
        if (!state.isCheckingAuth) {
            NavigationRoot(
                navController = navController,
                startDestination = if (state.isLoggedIn) {
                    ChatGraphRoute.Graph
                } else {
                    AuthGraphRoutes.Graph
                },
            )
            DeepLinkListener(navController)
        }
    }
}