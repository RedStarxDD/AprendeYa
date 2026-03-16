package com.usil.aprendeya.data.model

data class Cuestionario(
    val id: String? = null,
    val titulo: String? = null,
    val preguntas: List<Pregunta>? = null
)
