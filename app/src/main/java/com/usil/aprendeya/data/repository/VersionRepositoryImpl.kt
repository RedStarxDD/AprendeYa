package com.usil.aprendeya.data.repository

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.usil.aprendeya.domain.repository.VersionRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VersionRepositoryImpl @Inject constructor(
    private val appContext: Application
) : VersionRepository {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 })
    }

    override suspend fun getCurrentVersion(): List<Int> {
        return try {
            val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
            val versionName = packageInfo.versionName ?: "0.0.0"
            versionName.split(".").map { v -> v.toInt() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getMinAllowedVersion(): List<Int> {
        remoteConfig.fetchAndActivate().await()
        val minVersion = remoteConfig.getString("min_version")
        if (minVersion.isBlank()) return emptyList()

        return minVersion.split(".").map { v -> v.toInt() }
    }
}