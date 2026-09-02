package ru.missclick.chat.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.cancel
import course_kmp.feature.chat.presentation.generated.resources.contact_support_change_email
import course_kmp.feature.chat.presentation.generated.resources.current_password
import course_kmp.feature.chat.presentation.generated.resources.delete
import course_kmp.feature.chat.presentation.generated.resources.delete_profile_picture
import course_kmp.feature.chat.presentation.generated.resources.delete_profile_picture_desk
import course_kmp.feature.chat.presentation.generated.resources.email
import course_kmp.feature.chat.presentation.generated.resources.new_password
import course_kmp.feature.chat.presentation.generated.resources.password
import course_kmp.feature.chat.presentation.generated.resources.password_changed_successfully
import course_kmp.feature.chat.presentation.generated.resources.password_hint
import course_kmp.feature.chat.presentation.generated.resources.profile_image
import course_kmp.feature.chat.presentation.generated.resources.save
import course_kmp.feature.chat.presentation.generated.resources.upload_icon
import course_kmp.feature.chat.presentation.generated.resources.upload_image
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.chat.presentation.profile.components.ProfileHeaderSection
import ru.missclick.chat.presentation.profile.components.ProfileSectionLayout
import ru.missclick.core.designsystem.components.avatar.AvatarSize
import ru.missclick.core.designsystem.components.avatar.CourseAvatarPhoto
import ru.missclick.core.designsystem.components.brand.CourseHorizontalDivider
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.buttons.CourseButtonStyle
import ru.missclick.core.designsystem.components.dialog.CourseAdaptiveDialogSheetLayout
import ru.missclick.core.designsystem.components.dialog.DestructiveConfirmationDialog
import ru.missclick.core.designsystem.components.textfields.CoursePasswordTextField
import ru.missclick.core.designsystem.components.textfields.CourseTextField
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended
import ru.missclick.core.presentation.util.clearFocusOnTap
import ru.missclick.core.presentation.util.currentDeviceConfiguration

@Composable
fun ProfileRoot(
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CourseAdaptiveDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ProfileScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    is ProfileAction.OnDismiss -> onDismiss()
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeaderSection(
            username = state.username,
            onCloseClick = {
                onAction(ProfileAction.OnDismiss)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 16.dp,
                    horizontal = 12.dp
                )
        )
        CourseHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.profile_image),
        ) {
            Row {
                CourseAvatarPhoto(
                    displayText = state.userInitials,
                    size = AvatarSize.LARGE,
                    imageUrl = state.profilePictureUrl,
                    onClick = {
                        onAction(ProfileAction.OnUploadPictureClick)
                    }
                )
                Spacer(Modifier.width(20.dp))
                FlowRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CourseButton(
                        text = stringResource(Res.string.upload_image),
                        onClick = {
                            onAction(ProfileAction.OnUploadPictureClick)
                        },
                        style = CourseButtonStyle.SECONDARY,
                        enabled = !state.isUploadingImage && !state.isDeletingImage,
                        isLoading = state.isUploadingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.upload_icon),
                                contentDescription = stringResource(Res.string.upload_image)
                            )
                        }
                    )

                    CourseButton(
                        text = stringResource(Res.string.delete),
                        onClick = {
                            onAction(ProfileAction.OnDeletePictureClick)
                        },
                        style = CourseButtonStyle.DESTRUCTIVE_SECONDARY,
                        enabled = !state.isUploadingImage && !state.isDeletingImage &&
                                state.profilePictureUrl != null,
                        isLoading = state.isDeletingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(Res.string.delete)
                            )
                        }
                    )
                }
            }

            if (state.imageError != null) {
                Text(
                    text = state.imageError.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        CourseHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.email)
        ) {
            CourseTextField(
                state = state.emailTextState,
                enabled = false,
                supportingText = stringResource(Res.string.contact_support_change_email)
            )
        }
        ProfileSectionLayout(
            headerText = stringResource(Res.string.password)
        ) {
            CoursePasswordTextField(
                state = state.currentPasswordTextState,
                isPasswordVisible = state.isCurrentPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ProfileAction.OnToggleCurrentPasswordVisibility)
                },
                placeholder = stringResource(Res.string.current_password),
                isError = state.newPasswordError != null
            )
            CoursePasswordTextField(
                state = state.newPasswordTextState,
                isPasswordVisible = state.isNewPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ProfileAction.OnToggleNewPasswordVisibility)
                },
                placeholder = stringResource(Res.string.new_password),
                isError = state.newPasswordError != null,
                supportingText = state.newPasswordError?.asString()
                    ?: stringResource(Res.string.password_hint)
            )
            if (state.isPasswordChangeSuccessful) {
                Text(
                    text = stringResource(Res.string.password_changed_successfully),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.extended.success,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
            ) {
                CourseButton(
                    text = stringResource(Res.string.cancel),
                    style = CourseButtonStyle.SECONDARY,
                    onClick = {
                        onAction(ProfileAction.OnDismiss)
                    }
                )
                CourseButton(
                    text = stringResource(Res.string.save),
                    onClick = {
                        onAction(ProfileAction.OnChangePasswordClick)
                    },
                    enabled = state.canChangePassword,
                    isLoading = state.isChangingPassword
                )
            }
        }
        val currentDeviceConfiguration = currentDeviceConfiguration()
        if (currentDeviceConfiguration.isMobile) {
            Spacer(Modifier.weight(1f))
        }
    }

    if (state.showDeleteConfirmationDialog) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.delete_profile_picture),
            description = stringResource(Res.string.delete_profile_picture_desk),
            confirmButtonText = stringResource(Res.string.delete),
            cancelButtonText = stringResource(Res.string.cancel),
            onConfirmClick = {
                onAction(ProfileAction.OnConfirmDeleteClick)
            },
            onCancelClick = {
                onAction(ProfileAction.OnDismissDeleteConfirmationDialogClick)
            },
            onDismiss = {
                onAction(ProfileAction.OnDismissDeleteConfirmationDialogClick)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CourseTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}