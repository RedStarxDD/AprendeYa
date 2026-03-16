package com.usil.aprendeya.ui.screens.alumno

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.usil.aprendeya.data.model.Pregunta
import com.usil.aprendeya.data.response.FirestoreResponse
import com.usil.aprendeya.ui.screens.components.EmptyContent
import com.usil.aprendeya.ui.screens.components.LoadingCircle
import com.usil.aprendeya.ui.theme.FondoCuestionario
import com.usil.aprendeya.ui.theme.Pregunta
import com.usil.aprendeya.ui.theme.RojoOscuro
import com.usil.aprendeya.ui.theme.VerdeOscuro
import com.usil.aprendeya.viewModel.alumno.PreguntaViewModel

@Composable
fun PreguntaScreen(
    viewModel: PreguntaViewModel,
    paddingValues: PaddingValues,
    cursoId: String,
    temaId: String,
    cuestionarioId: String
) {
    val preguntaState by viewModel.preguntas.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = FondoCuestionario)
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        LaunchedEffect(Unit) {
            viewModel.getPreguntas(cursoId, temaId, cuestionarioId)
        }

        when (val result = preguntaState) {
            is FirestoreResponse.Error -> {
                EmptyContent(result.message)
            }

            FirestoreResponse.Loading -> {
                LoadingCircle("Cargando pregunta")
            }

            is FirestoreResponse.Success -> {
                if (result.data.isEmpty()) {
                    EmptyContent("No se encontraron preguntas")
                } else {
                    PreguntaContent(result.data, viewModel)
                }

            }
        }
    }
}

@Composable
private fun PreguntaContent(preguntas: List<Pregunta>, viewModel: PreguntaViewModel) {
    var selectedAlternativa by remember { mutableIntStateOf(-1) }
    val currentPregunta by viewModel.currentPregunta.collectAsState()
    val alternativas by viewModel.alternativas.collectAsState()
    val terminado by viewModel.terminado.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Título del cuestionario",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
        )
        if (terminado) {
            CartaPregunta("Cuestionario terminado")
        } else {
            CartaPregunta(
                question = preguntas[currentPregunta].enunciado.orEmpty()
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                alternativas.forEachIndexed { indice, elemento ->
                    CartaAlternativa(
                        letra = ('A' + indice).toString(),
                        texto = elemento.first,
                        selected = selectedAlternativa == indice,
                        onClick = { selectedAlternativa = indice }
                    )
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro,
                    disabledContainerColor = VerdeOscuro.copy(alpha = 0.25f)
                ),
                enabled = selectedAlternativa >= 0,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.onAlternativaSelected(selectedAlternativa)
                    selectedAlternativa = -1
                }
            ) {
                Text(
                    text = "Confirmar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun CartaPregunta(question: String) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Pregunta
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            maxLines = 5
        )
    }
}

@Composable
fun CartaAlternativa(
    letra: String,
    texto: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) RojoOscuro else Color.White
        ),
        modifier = Modifier
            .height(70.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = if (selected) Color.White else RojoOscuro,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letra,
                    color = if (selected) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) Color.White else Color.Black,
                maxLines = 2
            )
        }
    }
}