package ru.missclick.core.designsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import course_kmp.core.designsystem.generated.resources.Res
import course_kmp.core.designsystem.generated.resources.success_checkmark
import org.jetbrains.compose.resources.vectorResource
import ru.missclick.core.designsystem.theme.extended

@Composable
fun CourseSuccessIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = vectorResource(Res.drawable.success_checkmark),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.extended.success,
        modifier = modifier
    )
}