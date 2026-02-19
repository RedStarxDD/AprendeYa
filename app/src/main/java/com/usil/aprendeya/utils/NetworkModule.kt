package com.usil.aprendeya.utils

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.usil.aprendeya.data.repository.AuthRepositoryImpl
import com.usil.aprendeya.data.repository.CursoRepositoryImpl
import com.usil.aprendeya.domain.repository.AuthRepository
import com.usil.aprendeya.domain.repository.CursoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFirebase(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideCursoRepository(auth: AuthRepositoryImpl, db: FirebaseFirestore): CursoRepository {
        return CursoRepositoryImpl(auth, db)
    }
}