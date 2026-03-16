package com.usil.aprendeya.data.repository

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.usil.aprendeya.data.model.Cuestionario
import com.usil.aprendeya.data.model.Curso
import com.usil.aprendeya.data.model.Pregunta
import com.usil.aprendeya.data.model.Tema
import com.usil.aprendeya.data.model.Tutoria
import com.usil.aprendeya.data.response.FirestoreResponse
import com.usil.aprendeya.domain.repository.AuthRepository
import com.usil.aprendeya.domain.repository.CursoRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CursoRepositoryImpl @Inject constructor(
    private val auth: AuthRepository,
    private val db: FirebaseFirestore
) : CursoRepository {
    override suspend fun getAllCursos(): FirestoreResponse<List<Curso>> {
        return try {
            val uid = auth.getUid() ?: return FirestoreResponse.Error("Usuario no autenticado")

            val cursosId = getCursosIdDeUsuario(uid)

            if (cursosId.isEmpty()) {
                return FirestoreResponse.Success(emptyList())
            }

            val cursos = db.collection("cursos")
                .whereIn(FieldPath.documentId(), cursosId)
                .get()
                .await()
                .documents
                .mapNotNull { s ->
                    s.toObject(Curso::class.java)?.copy(id = s.id)
                }

            FirestoreResponse.Success(cursos)
        } catch (e: Exception) {
            FirestoreResponse.Error("Error al obtener los cursos", e)
        }
    }

    override suspend fun getCursosIdDeUsuario(uid: String): List<String> {
        val usuarioSnapshot = db.collection("usuarios")
            .document(uid)
            .get()
            .await()

        return usuarioSnapshot.get("cursos") as? List<String> ?: emptyList()
    }

    override suspend fun getAllTemas(cursoId: String): FirestoreResponse<List<Tema>> {
        return try {
            val snapshot = db.collection("cursos")
                .document(cursoId)
                .collection("temas")
                .get()
                .await()

            if (snapshot.isEmpty) {
                return FirestoreResponse.Success(emptyList())
            }

            val temas = snapshot
                .documents
                .mapNotNull { s -> s.toObject(Tema::class.java)?.copy(id = s.id) }

            FirestoreResponse.Success(temas)
        } catch (e: Exception) {
            FirestoreResponse.Error("Error al obtener los temas")
        }
    }

    override suspend fun getAllTutorias(
        cursoId: String,
        temaId: String
    ): FirestoreResponse<List<Tutoria>> {
        return try {
            val snapshot = db.collection("cursos")
                .document(cursoId)
                .collection("temas")
                .document(temaId)
                .collection("tutorias")
                .get()
                .await()

            if (snapshot.isEmpty) {
                return FirestoreResponse.Success(emptyList())
            }

            val tutorias = snapshot
                .documents
                .mapNotNull { s -> s.toObject(Tutoria::class.java)?.copy(id = s.id) }

            FirestoreResponse.Success(tutorias)
        } catch (e: Exception) {
            FirestoreResponse.Error("Error al obtener los videos")
        }
    }

    override suspend fun getAllCuestionarios(
        cursoId: String,
        temaId: String
    ): FirestoreResponse<List<Cuestionario>> {
        return try {
            val snapshot = db.collection("cursos")
                .document(cursoId)
                .collection("temas")
                .document(temaId)
                .collection("cuestionarios")
                .get()
                .await()

            if (snapshot.isEmpty) {
                return FirestoreResponse.Success(emptyList())
            }

            val tutorias = snapshot
                .documents
                .mapNotNull { s -> s.toObject(Cuestionario::class.java)?.copy(id = s.id) }

            FirestoreResponse.Success(tutorias)
        } catch (e: Exception) {
            FirestoreResponse.Error("Error al obtener los cuestionarios")
        }
    }

    override suspend fun getPreguntas(
        cursoId: String,
        temaId: String,
        cuestionarioId: String
    ): FirestoreResponse<List<Pregunta>> {
        return try {
            val snapshot = db.collection("cursos")
                .document(cursoId)
                .collection("temas")
                .document(temaId)
                .collection("cuestionarios")
                .document(cuestionarioId)
                .collection("preguntas")
                .get()
                .await()

            if (snapshot.isEmpty) {
                return FirestoreResponse.Success(emptyList())
            }

            val preguntas = snapshot
                .documents
                .mapNotNull { s -> s.toObject(Pregunta::class.java)?.copy(id = s.id) }

            FirestoreResponse.Success(preguntas)
        } catch (e: Exception) {
            FirestoreResponse.Error("Error al obtener los cuestionarios")
        }
    }
}
