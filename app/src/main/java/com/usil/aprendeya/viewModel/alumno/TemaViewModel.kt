package com.usil.aprendeya.viewModel.alumno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.data.model.Cuestionario
import com.usil.aprendeya.data.model.Tutoria
import com.usil.aprendeya.data.response.FirestoreResponse
import com.usil.aprendeya.domain.repository.CursoRepository
import com.usil.aprendeya.ui.screens.components.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias TutoriaResponse = FirestoreResponse<List<Tutoria>>
typealias CuestionarioResponse = FirestoreResponse<List<Cuestionario>>

@HiltViewModel
class TemaViewModel @Inject constructor(
    private val repository: CursoRepository
) : ViewModel() {
    private val _tutorias = MutableStateFlow<TutoriaResponse>(FirestoreResponse.Loading)
    val tutorias: StateFlow<TutoriaResponse> = _tutorias.asStateFlow()
    private val _cuestionarios = MutableStateFlow<CuestionarioResponse>(FirestoreResponse.Loading)
    val cuestionarios: StateFlow<CuestionarioResponse> = _cuestionarios.asStateFlow()

    private val _event = MutableSharedFlow<AppEvent>()
    val event = _event.asSharedFlow()

    fun getTutorias(cursoId: String, temaId: String) = viewModelScope.launch {
        _tutorias.value = FirestoreResponse.Loading
        when (val result = repository.getAllTutorias(cursoId, temaId)) {
            is FirestoreResponse.Success -> {
                _tutorias.value = FirestoreResponse.Success(result.data)
            }

            is FirestoreResponse.Error -> {
                _tutorias.value = FirestoreResponse.Error(result.message)
            }

            FirestoreResponse.Loading -> TODO()
        }
    }

    fun getCuestionarios(cursoId: String, temaId: String) = viewModelScope.launch {
        _cuestionarios.value = FirestoreResponse.Loading
        when (val result = repository.getAllCuestionarios(cursoId, temaId)) {
            is FirestoreResponse.Success -> {
                _cuestionarios.value = FirestoreResponse.Success(result.data)
            }

            is FirestoreResponse.Error -> {
                _cuestionarios.value = FirestoreResponse.Error(result.message)
            }

            FirestoreResponse.Loading -> TODO()
        }
    }

    fun onTutoriasSelected(url: String) = viewModelScope.launch {
        _event.emit(AppEvent.OpenLink(url))
    }

    fun onCuestionariosSelected(cursoId: String, temaId: String, cuestionarioId: String) = viewModelScope.launch {
        _event.emit(AppEvent.ToPregunta(cursoId, temaId, cuestionarioId))
    }
}