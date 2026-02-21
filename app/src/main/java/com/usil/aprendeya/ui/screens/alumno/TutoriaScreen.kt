package com.usil.aprendeya.ui.screens.alumno

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usil.aprendeya.data.model.Tutoria
import com.usil.aprendeya.ui.screens.components.EmptyContent
import com.usil.aprendeya.ui.screens.components.TitleInstructions
import com.usil.aprendeya.ui.screens.components.LoadingCircle
import com.usil.aprendeya.ui.theme.RojoOscuro
import com.usil.aprendeya.viewModel.alumno.TutoriaViewModel

@Composable
fun TutoriaScreen(viewModel: TutoriaViewModel, paddingValues: PaddingValues, idCurso: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LaunchedEffect(Unit) {
            viewModel.getTutorias(idCurso)
            viewModel.event.collect {}
        }
        TitleInstructions(
            "Videos disponibles",
            "En esta sección encontrarás videos sobre los temas aprendidos en clase.",
            "Presiona un video para abrirlo en tu reproductor de preferencia:"
        )
        TutoriaLista(viewModel)
    }
}

@Composable
private fun TutoriaLista(viewModel: TutoriaViewModel) {
    val tutorias: State<List<Tutoria>> = viewModel.tutorias.collectAsState()
    val isLoading: Boolean by viewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingCircle("Cargando lista de videos")
    } else {
        if (tutorias.value.isEmpty()) {
            EmptyContent("No hay videos disponibles")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                items(tutorias.value) { tutoria ->
                    TutoriaItem(tutoria) {
                        viewModel.onTutoriaClicked(tutoria.video.orEmpty())
                    }
                }
            }
        }
    }
}

@Composable
private fun TutoriaItem(tutoria: Tutoria, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledIconButton(
            onClick = { onClick() },
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = RojoOscuro,
                contentColor = Color.White
            ),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Reproducir",
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = tutoria.titulo.orEmpty(),
            fontSize = 16.sp,
            maxLines = 2,
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}