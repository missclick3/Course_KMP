package ru.missclick.core.designsystem.components.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.missclick.core.designsystem.theme.CourseTheme

@Composable
fun CourseStackedAvatar(
    avatars: List<ChatParticipantUi>,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.SMALL,
    maxVisible: Int = 2,
    overlapPercentage: Float = 0.4f
) {
    val overlapOffset = -(size.dp * overlapPercentage)

    val visibleAvatars = avatars.take(maxVisible)
    val remainingCount = (avatars.size - maxVisible).coerceAtLeast(0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlapOffset),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleAvatars.forEach { avatarUi ->
            CourseAvatarPhoto(
                displayText = avatarUi.initials,
                size = size,
                imageUrl = avatarUi.imageUrl,
            )
        }

        if (remainingCount > 0) {
            CourseAvatarPhoto(
                displayText = "$remainingCount+",
                size = size,
                textColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@Preview
fun CourseStackedAvatarPreview() {
    CourseTheme {
        CourseStackedAvatar(
            avatars = listOf(
                ChatParticipantUi(
                    "1",
                    username = "Alex",
                    initials = "AA"
                ),
                ChatParticipantUi(
                    "2",
                    username = "Ann",
                    initials = "AV"
                ),
                ChatParticipantUi(
                    "3",
                    username = "Alex",
                    initials = "AA"
                )
            )
        )
    }
}