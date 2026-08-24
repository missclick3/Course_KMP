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
import org.jetbrains.compose.resources.painterResource

import course_kmp.shared.generated.resources.Res
import course_kmp.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.missclick.auth.presentation.register.RegisterAction
import ru.missclick.auth.presentation.register.RegisterRoot
import ru.missclick.auth.presentation.registerSuccess.RegisterSuccessRoot
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.course_kmp.navigation.NavigationRoot

@Composable
@Preview
fun App() {
    CourseTheme {
        NavigationRoot()
    }
}