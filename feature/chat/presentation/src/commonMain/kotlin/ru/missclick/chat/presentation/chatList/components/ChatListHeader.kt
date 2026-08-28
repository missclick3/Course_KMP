package ru.missclick.chat.presentation.chatList.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import course_kmp.core.designsystem.generated.resources.log_out_icon
import course_kmp.core.designsystem.generated.resources.logo_chirp
import course_kmp.core.designsystem.generated.resources.users_icon
import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.logout
import course_kmp.feature.chat.presentation.generated.resources.profile_settings
import org.jetbrains.compose.resources.stringResource
import course_kmp.core.designsystem.generated.resources.Res as DesignSystemRes
import org.jetbrains.compose.resources.vectorResource
import ru.missclick.chat.presentation.components.ChatHeader
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi
import ru.missclick.core.designsystem.components.avatar.CourseAvatarPhoto
import ru.missclick.core.designsystem.components.brand.CourseHorizontalDivider
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended

@Composable
fun ChatListHeader(
    localParticipant: ChatParticipantUi?,
    isUserMenuOpen: Boolean,
    onUserAvatarClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChatHeader(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = vectorResource(DesignSystemRes.drawable.logo_chirp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "Course",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Spacer(Modifier.weight(1f))
            ProfileAvatarSection(
                localParticipant = localParticipant,
                isMenuOpen = isUserMenuOpen,
                onClick = onUserAvatarClick,
                onDismissMenu = onDismissMenu,
                onProfileSettingsClick = onProfileSettingsClick,
                onLogoutClick = onLogoutClick,
            )
        }
    }
}

@Composable
fun ProfileAvatarSection(
    localParticipant: ChatParticipantUi?,
    isMenuOpen: Boolean,
    onClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        if (localParticipant != null) {
            CourseAvatarPhoto(
                displayText = localParticipant.initials,
                imageUrl = localParticipant.imageUrl,
                onClick = onClick
            )
        }

        DropdownMenu(
            expanded = isMenuOpen,
            shape = RoundedCornerShape(16.dp),
            onDismissRequest = onDismissMenu,
            containerColor = MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.extended.surfaceOutline
            )
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(DesignSystemRes.drawable.users_icon),
                            contentDescription = stringResource(Res.string.profile_settings),
                            tint = MaterialTheme.colorScheme.extended.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(Res.string.profile_settings),
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    onDismissMenu()
                    onProfileSettingsClick()
                }
            )
            CourseHorizontalDivider()
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(DesignSystemRes.drawable.log_out_icon),
                            contentDescription = stringResource(Res.string.logout),
                            tint = MaterialTheme.colorScheme.extended.destructiveHover,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(Res.string.logout),
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    onDismissMenu()
                    onLogoutClick()
                }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun ChatListHeaderPreview() {
    CourseTheme {
        Box(
            Modifier.fillMaxSize()
        ) {
            ChatListHeader(
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Alex",
                    initials = "AA",
                ),
                isUserMenuOpen = true,
                onUserAvatarClick = {},
                onDismissMenu = { },
                onProfileSettingsClick = {},
                onLogoutClick = {},
            )
        }
    }
}