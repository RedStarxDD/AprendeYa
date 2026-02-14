package com.usil.aprendeya.data.model

data class Curso(
    val id: String? = null,
    val nombre: String? = null,
    //val temas: List<Tema>? = null,
    val tutorias: List<Tutoria>? = null
)

