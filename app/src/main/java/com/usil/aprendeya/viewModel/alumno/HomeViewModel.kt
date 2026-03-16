package com.usil.aprendeya.viewModel.alumno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.data.model.Curso
import com.usil.aprendeya.data.response.FirestoreResponse
import com.usil.aprendeya.domain.repository.CursoRepository
import com.usil.aprendeya.ui.screens.components.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias CursosResponse = FirestoreResponse<List<Curso>>

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CursoRepository
) : ViewModel() {
    private val _cursos = MutableStateFlow<CursosResponse>(FirestoreResponse.Loading)
    val cursos: StateFlow<CursosResponse> = _cursos
    private val _event = MutableSharedFlow<AppEvent>()
    val event = _event.asSharedFlow()

    fun getCursos() = viewModelScope.launch {
        _cursos.value=FirestoreResponse.Loading

        when (val result = repository.getAllCursos()) {
            is FirestoreResponse.Success -> {
                _cursos.value = FirestoreResponse.Success(result.data)
            }

            is FirestoreResponse.Error -> {
                _cursos.value = FirestoreResponse.Error(result.message)
            }

            FirestoreResponse.Loading -> TODO()
        }
    }

    fun onCursoItemSelected(idCurso: String, nombreCurso: String) = viewModelScope.launch {
        _event.emit(AppEvent.ToCurso(idCurso, nombreCurso))
    }
}