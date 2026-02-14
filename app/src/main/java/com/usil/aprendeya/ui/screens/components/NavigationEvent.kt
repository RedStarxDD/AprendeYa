package com.usil.aprendeya.ui.screens.components

import com.usil.aprendeya.data.model.Curso

sealed interface NavigationEvent {
    data object ToHome : NavigationEvent
    data object ToLogin : NavigationEvent
    data class ToCurso(val curso: Curso) : NavigationEvent
    data object ToTutoria : NavigationEvent
}
