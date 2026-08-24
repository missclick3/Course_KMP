package ru.missclick.auth.presentation.emailVerification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import course_kmp.feature.auth.presentation.generated.resources.Res
import course_kmp.feature.auth.presentation.generated.resources.close
import course_kmp.feature.auth.presentation.generated.resources.email_verification_failed
import course_kmp.feature.auth.presentation.generated.resources.email_verification_failed_desc
import course_kmp.feature.auth.presentation.generated.resources.email_verified_successfully
import course_kmp.feature.auth.presentation.generated.resources.email_verified_successfully_desc
import course_kmp.feature.auth.presentation.generated.resources.login
import course_kmp.feature.auth.presentation.generated.resources.verifying_account
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.core.designsystem.components.brand.CourseFailureIcon
import ru.missclick.core.designsystem.components.brand.CourseSuccessIcon
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.buttons.CourseButtonStyle
import ru.missclick.core.designsystem.components.layouts.CourseAdaptiveResultLayout
import ru.missclick.core.designsystem.components.layouts.CourseSimpleResultLayout
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended

@Composable
fun EmailVerificationRoot(
    viewModel: EmailVerificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmailVerificationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EmailVerificationScreen(
    state: EmailVerificationState,
    onAction: (EmailVerificationAction) -> Unit,
) {
    CourseAdaptiveResultLayout {
        when {
            state.isVerifying -> {
                VerifyingContent(Modifier.fillMaxWidth())
            }
            state.isVerified -> {
                CourseSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_successfully),
                    description = stringResource(Res.string.email_verified_successfully_desc),
                    icon = {
                        CourseSuccessIcon()
                    },
                    primaryButton = {
                        CourseButton(
                            text = stringResource(Res.string.login),
                            onClick = {
                                onAction(EmailVerificationAction.OnLoginClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }
            else -> {
                CourseSimpleResultLayout(
                    title = stringResource(Res.string.email_verification_failed),
                    description = stringResource(Res.string.email_verification_failed_desc),
                    icon = {
                        Spacer(Modifier.height(32.dp))
                        CourseFailureIcon(
                            modifier = Modifier
                                .size(80.dp)
                        )
                        Spacer(Modifier.height(32.dp))
                    },
                    primaryButton = {
                        CourseButton(
                            text = stringResource(Res.string.close),
                            onClick = {
                                onAction(EmailVerificationAction.OnCloseClick)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            style = CourseButtonStyle.SECONDARY
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun VerifyingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .heightIn(
                min = 200.dp,
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(Res.string.verifying_account),
            color = MaterialTheme.colorScheme.extended.textSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CourseTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = true
            ),
            onAction = {}
        )
    }
}