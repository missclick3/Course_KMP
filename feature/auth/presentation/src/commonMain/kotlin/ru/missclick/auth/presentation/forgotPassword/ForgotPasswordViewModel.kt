package ru.missclick.auth.presentation.forgotPassword

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.missclick.auth.domain.EmailValidator
import ru.missclick.core.domain.auth.AuthService
import ru.missclick.core.domain.util.onFailure
import ru.missclick.core.domain.util.onSuccess
import ru.missclick.core.presentation.util.toUiText

class ForgotPasswordViewModel(
    private val authService: AuthService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ForgotPasswordState())

    private val isEmailValidFlow = snapshotFlow { state.value.emailTextFieldState.text.toString() }
        .map { email -> EmailValidator.validate(email) }
        .distinctUntilChanged()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeValidationState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ForgotPasswordState()
        )

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            ForgotPasswordAction.OnSubmitClick -> submitForgotPasswordRequest()
        }
    }

    private fun observeValidationState() {
        isEmailValidFlow.onEach { isEmailValid ->
            _state.update {
                it.copy(
                    canSubmit = isEmailValid
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun submitForgotPasswordRequest() {
        if (state.value.isLoading || !state.value.canSubmit) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isEmailSendSuccessfully = false,
                    errorText = null
                )
            }

            val email = state.value.emailTextFieldState.text.toString()

            authService
                .forgotPassword(
                    email = email
                ).onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isEmailSendSuccessfully = true,
                        )
                    }
                }.onFailure { error ->
                    _state.update {
                        it.copy(
                            errorText = error.toUiText(),
                            isLoading = false
                        )
                    }
                }
        }
    }

}