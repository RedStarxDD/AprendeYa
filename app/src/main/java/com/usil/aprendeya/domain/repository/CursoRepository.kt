package com.usil.aprendeya.domain.repository

import com.usil.aprendeya.data.model.Cuestionario
import com.usil.aprendeya.data.model.Curso
import com.usil.aprendeya.data.model.Pregunta
import com.usil.aprendeya.data.model.Tema
import com.usil.aprendeya.data.model.Tutoria
import com.usil.aprendeya.data.response.FirestoreResponse

interface CursoRepository {
    suspend fun getAllCursos(): FirestoreResponse<List<Curso>>
    suspend fun getCursosIdDeUsuario(uid: String): List<String>
    suspend fun getAllTemas(cursoId: String): FirestoreResponse<List<Tema>>
    suspend fun getAllTutorias(cursoId: String, temaId: String): FirestoreResponse<List<Tutoria>>
    suspend fun getAllCuestionarios(cursoId: String, temaId: String): FirestoreResponse<List<Cuestionario>>
    suspend fun getPreguntas(cursoId: String, temaId: String, cuestionarioId: String): FirestoreResponse<List<Pregunta>>
}