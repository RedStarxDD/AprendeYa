package com.usil.aprendeya.ui.screens.alumno

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.usil.aprendeya.data.model.Curso
import com.usil.aprendeya.data.response.FirestoreResponse
import com.usil.aprendeya.ui.screens.components.EmptyContent
import com.usil.aprendeya.ui.screens.components.LoadingCircle
import com.usil.aprendeya.ui.screens.components.TitleInstructions
import com.usil.aprendeya.ui.theme.Boton
import com.usil.aprendeya.viewModel.alumno.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LaunchedEffect(Unit) {
            viewModel.getCursos()
        }
        TitleInstructions(
            "Mis cursos",
            "Bienvenido a este segmento, en donde aprenderás a desarrollar tus conocimientos.",
            "Escoge un curso y comencemos esta aventura:"
        )
        CursoList(viewModel)
    }
}

@Composable
fun CursoList(viewModel: HomeViewModel) {
    val cursosState by viewModel.cursos.collectAsState()

    when (val result = cursosState) {
        is FirestoreResponse.Loading -> {
            LoadingCircle("Cargando lista de cursos")
        }

        is FirestoreResponse.Error -> {
            EmptyContent(result.message)
        }

        is FirestoreResponse.Success -> {
            val cursos = result.data

            if (cursos.isEmpty()) {
                EmptyContent("No hay cursos disponibles")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    items(cursos) { curso ->
                        CursoItem(curso) {
                            viewModel.onCursoItemSelected(
                                curso.id.orEmpty(),
                                curso.nombre.orEmpty()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CursoItem(curso: Curso, onCursoItemSelected: () -> Unit) {
    Button(
        onClick = { onCursoItemSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Boton
        )
    ) {
        Text(
            text = curso.nombre.orEmpty(),
            style = MaterialTheme.typography.titleMedium
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
}