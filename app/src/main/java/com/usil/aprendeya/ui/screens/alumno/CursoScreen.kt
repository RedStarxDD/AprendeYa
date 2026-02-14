package com.usil.aprendeya.ui.screens.alumno

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.usil.aprendeya.R
import com.usil.aprendeya.ui.theme.Boton
import com.usil.aprendeya.viewModel.alumno.CursoViewModel

@Composable
fun CursoScreen(viewModel: CursoViewModel, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(Unit) {
            viewModel.event.collect {}
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            CursoImage(modifier = Modifier.weight(1f), R.drawable.cuestionario, "Cuestionarios")
            Spacer(modifier = Modifier.padding(8.dp))
            CursoButton(
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f), "Cuestionarios"
            ){

            }
        }
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            CursoButton(
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                "Videos",
            ){
                viewModel.onTutoriasSelected()
            }
            Spacer(modifier = Modifier.padding(8.dp))
            CursoImage(modifier = Modifier.weight(1f), R.drawable.tutorias, "Videos")
        }
    }
}

@Composable
private fun CursoImage(modifier: Modifier, image: Int, contentDescription: String) {
    Image(
        painter = painterResource(image),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
private fun CursoButton(modifier: Modifier, title: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Boton,
            contentColor = Color.White
        )
    ) {
        Text(text = title)
    }
}