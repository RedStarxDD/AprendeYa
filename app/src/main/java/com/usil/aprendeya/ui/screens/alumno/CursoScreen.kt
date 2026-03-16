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
import com.usil.aprendeya.data.model.Tema
import com.usil.aprendeya.data.response.FirestoreResponse
import com.usil.aprendeya.ui.screens.components.EmptyContent
import com.usil.aprendeya.ui.screens.components.LoadingCircle
import com.usil.aprendeya.ui.screens.components.TitleInstructions
import com.usil.aprendeya.ui.theme.Boton
import com.usil.aprendeya.viewModel.alumno.CursoViewModel

@Composable
fun CursoScreen(viewModel: CursoViewModel, paddingValues: PaddingValues, idCurso: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LaunchedEffect(Unit) {
            viewModel.getTemas(idCurso)
        }
        TitleInstructions(
            "Temas disponibles",
            "Aquí está la lista de los temas de los cursos",
            "Navega entre cada uno para visualizar su contenido"
        )
        CursoLista(viewModel, idCurso)
    }
}

@Composable
private fun CursoLista(viewModel: CursoViewModel, idCurso: String) {
    val temasState by viewModel.temas.collectAsState()

    when (val result = temasState) {
        is FirestoreResponse.Loading -> {
            LoadingCircle("Cargando lista de temas")
        }

        is FirestoreResponse.Error -> {
            EmptyContent(result.message)
        }

        is FirestoreResponse.Success -> {
            val temas = result.data

            if (temas.isEmpty()) {
                EmptyContent("No hay temas disponibles")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    items(temas) { tema ->
                        CursoItem(tema) {
                            viewModel.onTemaItemSelected(idCurso, tema.id.orEmpty())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CursoItem(tema: Tema, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Boton
        )
    ) {
        Text(
            text = tema.nombre.orEmpty(),
            style = MaterialTheme.typography.titleMedium
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
}
