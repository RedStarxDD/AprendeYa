package com.usil.aprendeya.viewModel.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.data.response.LoginResult
import com.usil.aprendeya.domain.repository.AuthRepository
import com.usil.aprendeya.ui.screens.components.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    private val _loginEnabled = MutableStateFlow(false)
    val loginEnabled: StateFlow<Boolean> = _loginEnabled.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _loginError = MutableStateFlow("")
    val loginError: StateFlow<String> = _loginError.asStateFlow()

    private val _event = MutableSharedFlow<NavigationEvent>()
    val event = _event.asSharedFlow()

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnabled.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean = password.length > 6

    fun onLoginSelected() = viewModelScope.launch {
        _isLoading.value = true

        when (authRepository.login(_email.value, _password.value)) {
            LoginResult.Success -> {
                _event.emit(NavigationEvent.ToHome)
            }
            LoginResult.Error.InvalidCredentials -> {
                _loginError.value = "Correo o contraseña incorrectos"
            }
            LoginResult.Error.UserNotFound -> {
                _loginError.value = "El usuario no existe"
            }
            LoginResult.Error.Network -> {
                _loginError.value = "Error de conexión"
            }
            LoginResult.Error.Unknown -> {
                _loginError.value = "Error inesperado"
            }
        }
        _isLoading.value = false
    }
}
