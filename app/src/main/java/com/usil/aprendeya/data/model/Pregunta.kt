package com.usil.aprendeya.data.model

data class Pregunta(
    val id: String? = null,
    val enunciado: String? = null,
    val alternativas: List<String>? = null,
    val respuesta: Int? = null
)
