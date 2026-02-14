package com.usil.aprendeya.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes:NavKey{
    @Serializable
    data object Login: Routes()
    @Serializable
    data object Home: Routes()
    @Serializable
    data object Profile: Routes()
    @Serializable
    data object Curso: Routes()
    @Serializable
    data object Tutoria: Routes()
    @Serializable
    data object Error: Routes()
}