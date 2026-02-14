package com.usil.aprendeya.viewModel.alumno

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.usil.aprendeya.data.model.Curso
import com.usil.aprendeya.ui.screens.components.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _cursos = MutableStateFlow<List<Curso>>(emptyList())
    val cursos: StateFlow<List<Curso>> = _cursos.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _event = MutableSharedFlow<NavigationEvent>()
    val event = _event.asSharedFlow()

    init {
        getCursos()
    }

    private fun getCursos() = viewModelScope.launch {
        _isLoading.value = true
        val result: List<Curso> = withContext(Dispatchers.IO) {
            getAllCursos()
        }
        _cursos.value = result
        _isLoading.value = false
    }

    private suspend fun getAllCursos(): List<Curso> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: return emptyList()

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
            Log.e("home", "error", e)
            emptyList()
        }
    }

    fun onCursoItemSelected(curso: Curso) = viewModelScope.launch {
        _event.emit(NavigationEvent.ToCurso(curso))
        Log.e("home", curso.nombre.orEmpty())
    }
}