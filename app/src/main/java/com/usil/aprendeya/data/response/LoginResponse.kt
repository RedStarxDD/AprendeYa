package com.usil.aprendeya.data.response

sealed class LoginResult {
    data object Success : LoginResult()

    sealed class Error : LoginResult() {
        data object InvalidCredentials : Error()
        data object UserNotFound : Error()
        data object Network : Error()
        data object Unknown : Error()
    }
}