package com.usil.aprendeya.domain.repository

interface VersionRepository {
    suspend fun getCurrentVersion():List<Int>
    suspend fun getMinAllowedVersion():List<Int>
}