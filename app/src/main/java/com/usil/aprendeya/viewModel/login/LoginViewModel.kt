package com.usil.aprendeya.viewModel.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.data.response.LoginResult
import com.usil.aprendeya.domain.repository.AuthRepository
import com.usil.aprendeya.domain.repository.VersionRepository
import com.usil.aprendeya.ui.screens.components.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val versionRepository: VersionRepository
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
    private val _blockVersion=MutableStateFlow(false)
    val blockVersion:StateFlow<Boolean> = _blockVersion.asStateFlow()

    private val _event = MutableSharedFlow<AppEvent>()
    val event = _event.asSharedFlow()

    init {
        checkUserVersion()
    }

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
                _event.emit(AppEvent.ToHome)
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

    private fun checkUserVersion() = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) {
            canAccessToApp()
        }
        _blockVersion.value=!result
    }

    private suspend fun canAccessToApp(): Boolean {
        val currentVersion = versionRepository.getCurrentVersion()
        val minAllowedVersion = versionRepository.getMinAllowedVersion()

        for ((currentPart, minVersionPart) in currentVersion.zip(minAllowedVersion)) {
            if (currentPart != minVersionPart) {
                return currentPart > minVersionPart
            }
        }
        return true
    }

    fun closeOldVersionDialog(){
        _blockVersion.value=false
    }
}
