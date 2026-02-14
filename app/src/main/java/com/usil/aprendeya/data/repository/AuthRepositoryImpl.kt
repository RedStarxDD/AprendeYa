package com.usil.aprendeya.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.usil.aprendeya.data.response.LoginResult
import com.usil.aprendeya.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override suspend fun login(email: String, password: String): LoginResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            LoginResult.Success
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            LoginResult.Error.InvalidCredentials
        } catch (e: FirebaseAuthInvalidUserException) {
            LoginResult.Error.UserNotFound
        } catch (e: FirebaseNetworkException) {
            LoginResult.Error.Network
        } catch (e: Exception) {
            LoginResult.Error.Unknown
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }
}