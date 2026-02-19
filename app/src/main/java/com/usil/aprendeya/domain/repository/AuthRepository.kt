package com.usil.aprendeya.domain.repository

import com.usil.aprendeya.data.response.LoginResult

interface AuthRepository {
    suspend fun login(email: String, password: String): LoginResult
    suspend fun logout()
    suspend fun getUid():String?
}