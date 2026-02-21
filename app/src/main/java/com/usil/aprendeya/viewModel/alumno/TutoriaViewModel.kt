package com.usil.aprendeya.viewModel.alumno

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.data.model.Tutoria
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

@HiltViewModel
class TutoriaViewModel @Inject constructor(
    private val db: CursoRepository,
) : ViewModel() {
    private val _tutorias = MutableStateFlow<List<Tutoria>>(emptyList())
    val tutorias: StateFlow<List<Tutoria>> = _tutorias.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _event = MutableSharedFlow<AppEvent>()
    val event = _event.asSharedFlow()

    fun getTutorias(idCurso: String) = viewModelScope.launch {
        _isLoading.value = true
        val result: List<Tutoria> = db.getAllTutorias(idCurso)
        _tutorias.value = result
        _isLoading.value = false
    }

    fun onTutoriaClicked(url: String) = viewModelScope.launch {
        _event.emit(AppEvent.OpenLink(url))
    }
}