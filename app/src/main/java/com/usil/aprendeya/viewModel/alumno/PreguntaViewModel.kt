package com.usil.aprendeya.viewModel.alumno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.data.model.Pregunta
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

typealias PreguntasResponse = FirestoreResponse<List<Pregunta>>
typealias MixedAlternativas = List<Pair<String, Boolean>>

@HiltViewModel
class PreguntaViewModel @Inject constructor(
    private val repository: CursoRepository
) : ViewModel() {
    private val _preguntas = MutableStateFlow<PreguntasResponse>(FirestoreResponse.Loading)
    val preguntas: StateFlow<PreguntasResponse> = _preguntas.asStateFlow()
    private val _currentPregunta = MutableStateFlow(0)
    val currentPregunta: StateFlow<Int> = _currentPregunta.asStateFlow()
    private val _alternativas = MutableStateFlow<MixedAlternativas>(emptyList())
    val alternativas: StateFlow<MixedAlternativas> = _alternativas.asStateFlow()
    private val _terminado = MutableStateFlow(false)
    val terminado: StateFlow<Boolean> = _terminado.asStateFlow()

    private val _event = MutableSharedFlow<AppEvent>()
    val event = _event.asSharedFlow()

    fun getPreguntas(cursoId: String, temaId: String, cuestionarioId: String) =
        viewModelScope.launch {
            _preguntas.value = FirestoreResponse.Loading
            when (val result = repository.getPreguntas(cursoId, temaId, cuestionarioId)) {
                is FirestoreResponse.Success -> {
                    _preguntas.value = FirestoreResponse.Success(result.data)
                    _alternativas.value = mixAlternativas(result.data[0])
                }

                is FirestoreResponse.Error -> {
                    _preguntas.value = FirestoreResponse.Error(result.message)
                }

                FirestoreResponse.Loading -> TODO()
            }
        }

    private fun mixAlternativas(pregunta: Pregunta): List<Pair<String, Boolean>> {
        return pregunta.alternativas?.mapIndexed { indice, elemento ->
            Pair(elemento, indice == pregunta.respuesta)
        }?.shuffled() ?: emptyList()
    }

    fun onAlternativaSelected(index: Int) {
        val result = _preguntas.value
        if (result is FirestoreResponse.Success) {
            if (_alternativas.value[index].second) {
                nextPregunta()
            }
        }
    }

    private fun nextPregunta() = viewModelScope.launch {
        val result = _preguntas.value
        if (result is FirestoreResponse.Success) {
            if (currentPregunta.value < result.data.size - 1) {
                _currentPregunta.value++
                _alternativas.value = mixAlternativas(result.data[currentPregunta.value])

                //if(currentPregunta.value < result.data.size - 1)
            }
        }
    }
}
