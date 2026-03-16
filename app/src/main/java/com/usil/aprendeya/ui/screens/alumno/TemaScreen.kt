package com.usil.aprendeya.ui.screens.alumno

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.usil.aprendeya.R
import com.usil.aprendeya.data.model.Cuestionario
import com.usil.aprendeya.data.model.Tutoria
import com.usil.aprendeya.data.response.FirestoreResponse
import com.usil.aprendeya.ui.screens.components.EmptyContent
import com.usil.aprendeya.ui.screens.components.LoadingCircle
import com.usil.aprendeya.ui.theme.Boton
import com.usil.aprendeya.ui.theme.Naranja
import com.usil.aprendeya.ui.theme.RojoOscuro
import com.usil.aprendeya.viewModel.alumno.TemaViewModel

@Composable
fun TemaScreen(
    viewModel: TemaViewModel,
    paddingValues: PaddingValues,
    cursoId: String,
    temaId: String
) {
    val cuestionariosState by viewModel.cuestionarios.collectAsState()
    val tutoriasState by viewModel.tutorias.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(Unit) {
            viewModel.getTutorias(cursoId, temaId)
            viewModel.getCuestionarios(cursoId, temaId)
        }
        TemaTitle("Cuestionarios")
        when (val result = cuestionariosState) {
            is FirestoreResponse.Loading -> {
                LoadingCircle("Cargando lista de videos")
            }

            is FirestoreResponse.Error -> {
                EmptyContent(result.message)
            }

            is FirestoreResponse.Success -> {
                val cuestionarios = result.data

                if (cuestionarios.isEmpty()) {
                    EmptyContent("No hay cuestionarios disponibles")
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(cuestionarios) { cuestionario ->
                            CuestionarioItem(cuestionario) {
                                viewModel.onCuestionariosSelected(cursoId, temaId, cuestionario.id.orEmpty())
                            }
                        }
                    }
                }
            }
        }

        TemaTitle("Tutorías")
        when (val result = tutoriasState) {
            is FirestoreResponse.Loading -> {
                LoadingCircle("Cargando lista de videos")
            }

            is FirestoreResponse.Error -> {
                EmptyContent(result.message)
            }

            is FirestoreResponse.Success -> {
                val tutorias = result.data

                if (tutorias.isEmpty()) {
                    EmptyContent("No hay videos disponibles")
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(tutorias) { tutoria ->
                            VideoItem(tutoria) {
                                viewModel.onTutoriasSelected(tutoria.video.orEmpty())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TemaTitle(
    contentDescription: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = contentDescription,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Text(
            text = ">",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun CuestionarioItem(
    cuestionario: Cuestionario,
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(192.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Naranja),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_quiz_24),
                    contentDescription = "Reproducir",
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = cuestionario.titulo.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun VideoItem(
    tutoria: Tutoria,
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(192.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RojoOscuro),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Reproducir",
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = tutoria.titulo.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}