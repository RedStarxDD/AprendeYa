package com.usil.aprendeya.domain.repository

import com.usil.aprendeya.data.model.Curso
import com.usil.aprendeya.data.model.Tutoria

interface CursoRepository {
    suspend fun getAllCursos(): List<Curso>
    suspend fun getAllTutorias(idCurso: String): List<Tutoria>
}