package com.usil.aprendeya.ui.screens.components

import com.usil.aprendeya.data.model.Curso

sealed interface AppEvent {
    data object ToHome : AppEvent
    data object ToLogin : AppEvent
    data class ToCurso(val curso: Curso) : AppEvent
    data object ToTutoria : AppEvent

    data class OpenLink(val url: String) : AppEvent
}
