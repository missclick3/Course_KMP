package ru.missclick.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended

@Composable
fun CourseIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier
            .size(45.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
        )
    ) {
        content()
    }
}

@Preview
@Composable
private fun CourseIconButtonPreview() {
    CourseTheme {
        CourseIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun CourseIconButtonDarkThemePreview() {
    CourseTheme(
        darkTheme = true
    ) {
        CourseIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
            )
        }
    }
}