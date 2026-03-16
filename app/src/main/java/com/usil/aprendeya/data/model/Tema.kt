package com.usil.aprendeya.data.model

data class Tema(
    val id: String? = null,
    val nombre: String? = null,
    val tutorias: List<Tutoria>? = null,
    val cuestionarios: List<Cuestionario>? = null
)
