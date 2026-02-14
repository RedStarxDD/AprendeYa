package com.usil.aprendeya.viewModel.alumno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.aprendeya.ui.screens.components.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CursoViewModel @Inject constructor() : ViewModel() {
    private val _curso = MutableStateFlow("")
    val curso: StateFlow<String> = _curso.asStateFlow()

    private val _event = MutableSharedFlow<NavigationEvent>()
    val event = _event.asSharedFlow()

    fun onTutoriasSelected() = viewModelScope.launch {
        _event.emit(NavigationEvent.ToTutoria)
    }
}