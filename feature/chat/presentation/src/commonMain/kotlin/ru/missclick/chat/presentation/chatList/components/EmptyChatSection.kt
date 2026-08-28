package ru.missclick.chat.presentation.chatList.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.empty_chat
import course_kmp.feature.chat.presentation.generated.resources.no_messages
import course_kmp.feature.chat.presentation.generated.resources.no_messages_subtitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.missclick.core.designsystem.theme.extended
import ru.missclick.core.presentation.util.DeviceConfiguration
import ru.missclick.core.presentation.util.currentDeviceConfiguration

@Composable
fun EmptyChatSection(modifier: Modifier = Modifier) {
    val configuration = currentDeviceConfiguration()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.empty_chat),
            contentDescription = stringResource(Res.string.no_messages),
            modifier = Modifier.size(
                if (configuration == DeviceConfiguration.MOBILE_LANDSCAPE) {
                    125.dp
                } else 200.dp
            )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.no_messages),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.extended.textPrimary
        )
        Text(
            text = stringResource(Res.string.no_messages_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.extended.textSecondary
        )
    }
}