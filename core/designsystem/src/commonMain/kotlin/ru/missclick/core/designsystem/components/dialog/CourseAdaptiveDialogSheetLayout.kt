package ru.missclick.core.designsystem.components.dialog

import androidx.compose.runtime.Composable
import ru.missclick.core.presentation.util.currentDeviceConfiguration

@Composable
fun CourseAdaptiveDialogSheetLayout(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    val configuration = currentDeviceConfiguration()
    if (configuration.isMobile) {
        CourseBottomSheet(
            onDismiss = onDismiss,
            content = content
        )
    } else {
        CourseDialogContent(
            onDismiss = onDismiss,
            content = content
        )
    }
}