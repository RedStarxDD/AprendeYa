package com.usil.aprendeya.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.usil.aprendeya.data.model.Cuestionario
import kotlinx.serialization.Serializable

sealed class Routes : NavKey {
    @Serializable
    data object LoginRoute : Routes()

    @Serializable
    data object HomeRoute : Routes()

    @Serializable
    data object ProfileRoute : Routes()

    @Serializable
    data class CursoRoute(val cursoId: String) : Routes()

    @Serializable
    data class TemaRoute(val cursoId: String, val temaId: String) : Routes()

    @Serializable
    data class PreguntaRoute(val cursoId: String, val temaId: String, val cuestionarioId: String) : Routes()

    @Serializable
    data object ErrorRoute : Routes()
}