package ru.missclick.core.designsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import course_kmp.core.designsystem.generated.resources.Res
import course_kmp.core.designsystem.generated.resources.logo_chirp
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.missclick.core.designsystem.theme.CourseTheme

@Composable
fun CourseBrandLogo(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.logo_chirp),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
@Preview
fun CourseBrandLogoPreview() {
    CourseTheme {
        CourseBrandLogo()
    }
}