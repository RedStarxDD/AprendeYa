package com.usil.aprendeya.data.repository

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.usil.aprendeya.data.model.Curso
import com.usil.aprendeya.data.model.Tutoria
import com.usil.aprendeya.domain.repository.CursoRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CursoRepositoryImpl @Inject constructor(
    private val auth: AuthRepositoryImpl,
    private val db: FirebaseFirestore
) : CursoRepository {
    override suspend fun getAllCursos(): List<Curso> {
        return try {
            val uid = auth.getUid() ?: ""

            val usuarioSnapshot = db.collection("usuarios")
                .document(uid)
                .get()
                .await()

            val cursosId = usuarioSnapshot.get("cursos") as? List<String> ?: return emptyList()
            if (cursosId.isEmpty()) return emptyList()

            db.collection("cursos")
                .whereIn(FieldPath.documentId(), cursosId)
                .get()
                .await()
                .documents
                .mapNotNull { snapshot ->
                    snapshot.toObject(Curso::class.java)?.copy(id = snapshot.id)
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAllTutorias(idCurso: String): List<Tutoria> {
        return try {
            val snapshot = db.collection("cursos")
                .document(idCurso)
                .collection("tutorias")
                .get()
                .await()

            snapshot
                .documents
                .mapNotNull { s -> s.toObject(Tutoria::class.java) }

        } catch (e: Exception) {
            emptyList()
        }
    }
}