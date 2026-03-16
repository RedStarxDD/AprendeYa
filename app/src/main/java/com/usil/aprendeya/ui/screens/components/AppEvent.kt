package com.usil.aprendeya.ui.screens.components

sealed interface AppEvent {
    data object ToHome : AppEvent
    data object ToLogin : AppEvent
    data class ToCurso(val cursoId: String, val cursoNombre: String) : AppEvent
    data class ToTema(val cursoId: String, val temaId: String) : AppEvent
    data class ToPregunta(val cursoId: String, val temaId: String, val cuestionarioId: String) : AppEvent

    data class OpenLink(val url: String) : AppEvent
}