package ru.missclick.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import course_kmp.feature.auth.presentation.generated.resources.Res
import course_kmp.feature.auth.presentation.generated.resources.error_account_exists
import course_kmp.feature.auth.presentation.generated.resources.error_invalid_passport
import course_kmp.feature.auth.presentation.generated.resources.error_not_valid_email
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.missclick.auth.domain.EmailValidator
import ru.missclick.core.domain.auth.AuthService
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.onFailure
import ru.missclick.core.domain.util.onSuccess
import ru.missclick.core.domain.validation.PasswordValidator
import ru.missclick.core.presentation.util.UiText
import ru.missclick.core.presentation.util.toUiText

class RegisterViewModel(
    private val authService: AuthService
) : ViewModel() {

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterState()
        )

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnLoginClick -> Unit
            RegisterAction.OnRegisterClick -> register()
            RegisterAction.OnTogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }
            else -> Unit
        }
    }

    private fun register() {
        if (!validateFormInput()) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isRegistering = true
                )
            }

            val currentState= state.value
            val email = currentState.emailTextState.text.toString()
            val username = currentState.usernameTextState.text.toString()
            val password = currentState.passwordTextState.text.toString()

            authService.register(
                email = email,
                username = username,
                password = password
            ).onSuccess {
                _state.update {
                    it.copy(isRegistering = false)
                }
            }.onFailure { error ->
                val registrationError = when (error) {
                    DataError.Remote.CONFLICT -> UiText.Resource(Res.string.error_account_exists)
                    else -> error.toUiText()
                }
                _state.update {
                    it.copy(
                        isRegistering = false,
                        registrationError = registrationError
                    )
                }
            }
        }
    }

    private fun clearAllTextFieldErrors() {
        _state.update {
            it.copy(
                emailError = null,
                passwordError = null,
                usernameError = null,
                registrationError = null
            )
        }
    }

    private fun validateFormInput(): Boolean {
        clearAllTextFieldErrors()

        val currentState = state.value
        val email = currentState.emailTextState.text.toString()
        val username = currentState.usernameTextState.text.toString()
        val password = currentState.passwordTextState.text.toString()

        val isEmailValid = EmailValidator.validate(email)
        val passwordValidationState = PasswordValidator.validate(password)
        val isUsernameValid = username.length in 3..20

        val emailError = UiText.Resource(Res.string.error_not_valid_email)
            .takeIf { !isEmailValid }
        val passwordError = UiText.Resource(Res.string.error_invalid_passport)
            .takeIf { !passwordValidationState.isValidPassword }
        val usernameError = UiText.Resource(Res.string.error_account_exists)
            .takeIf { !isUsernameValid }

        _state.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                usernameError = usernameError
            )
        }

        return isUsernameValid && isEmailValid && passwordValidationState.isValidPassword
    }

}