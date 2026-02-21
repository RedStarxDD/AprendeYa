package com.usil.aprendeya.viewModel.alumno

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.data.model.Curso
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
class HomeViewModel @Inject constructor(
    private val db : CursoRepository
) : ViewModel() {
    private val _cursos = MutableStateFlow<List<Curso>>(emptyList())
    val cursos: StateFlow<List<Curso>> = _cursos.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _event = MutableSharedFlow<AppEvent>()
    val event = _event.asSharedFlow()

    fun getCursos() = viewModelScope.launch {
        _isLoading.value = true
        val result: List<Curso> = db.getAllCursos()
        _cursos.value = result
        _isLoading.value = false
    }

    fun onCursoItemSelected(curso: Curso) = viewModelScope.launch {
        _event.emit(AppEvent.ToCurso(curso))
    }
}