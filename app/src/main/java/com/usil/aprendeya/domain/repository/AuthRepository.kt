package com.usil.aprendeya.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.usil.aprendeya.data.response.LoginResult

interface AuthRepository {
    suspend fun register(email: String, password: String)
    suspend fun login(email: String, password: String): LoginResult
    suspend fun logout()
}