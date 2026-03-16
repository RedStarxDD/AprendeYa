package com.usil.aprendeya.viewModel.alumno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.data.model.Tema
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

typealias TemasResponse = FirestoreResponse<List<Tema>>

@HiltViewModel
class CursoViewModel @Inject constructor(
    private val repository: CursoRepository,
) : ViewModel() {
    private val _temas = MutableStateFlow<TemasResponse>(FirestoreResponse.Loading)
    val temas: StateFlow<TemasResponse> = _temas.asStateFlow()

    private val _event = MutableSharedFlow<AppEvent>()
    val event = _event.asSharedFlow()

    fun getTemas(cursoId: String) = viewModelScope.launch {
        _temas.value=FirestoreResponse.Loading

        when (val result = repository.getAllTemas(cursoId)) {
            is FirestoreResponse.Success -> {
                _temas.value = FirestoreResponse.Success(result.data)
            }

            is FirestoreResponse.Error -> {
                _temas.value = FirestoreResponse.Error(result.message)
            }

            FirestoreResponse.Loading -> TODO()
        }
    }

    fun onTemaItemSelected(cursoId: String, temaId: String) = viewModelScope.launch {
        _event.emit(AppEvent.ToTema(cursoId, temaId))
    }
}