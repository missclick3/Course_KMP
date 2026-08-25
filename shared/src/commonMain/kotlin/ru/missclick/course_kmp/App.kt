package ru.missclick.course_kmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource

import course_kmp.shared.generated.resources.Res
import course_kmp.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.auth.presentation.navigation.AuthGraphRoutes
import ru.missclick.auth.presentation.register.RegisterAction
import ru.missclick.auth.presentation.register.RegisterRoot
import ru.missclick.auth.presentation.registerSuccess.RegisterSuccessRoot
import ru.missclick.chat.presentation.chat_list.ChatListRoute
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.course_kmp.navigation.DeepLinkListener
import ru.missclick.course_kmp.navigation.NavigationRoot

@Composable
@Preview
fun App(
    onAuthenticationChecked: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    DeepLinkListener(navController)

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCheckingAuth) {
        if (!state.isCheckingAuth) {
            onAuthenticationChecked()
        }
    }

    CourseTheme {
        if (!state.isCheckingAuth) {
            NavigationRoot(
                navController = navController,
                startDestination = if (state.isLoggedIn) {
                    ChatListRoute
                } else {
                    AuthGraphRoutes.Graph
                },
            )
        }
    }
}