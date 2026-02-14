package com.usil.aprendeya.ui.screens.alumno

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.usil.aprendeya.R
import com.usil.aprendeya.ui.screens.login.HeaderImg
import com.usil.aprendeya.ui.theme.Boton
import com.usil.aprendeya.ui.theme.RojoOscuro
import com.usil.aprendeya.viewModel.alumno.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LaunchedEffect(Unit) {
            viewModel.event.collect {}
        }
    }
    HeaderImg()
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Logout(Modifier.align(Alignment.Center), viewModel)
    }
}

@Composable
fun Logout(modifier: Modifier, viewModel: ProfileViewModel) {
    val isLoading: Boolean by viewModel.isLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.padding(4.dp))
            Text(
                text = "Cerrando sesión",
                color = Boton,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Title(Modifier.align(Alignment.CenterHorizontally))
            ProfileImage(Modifier.align(Alignment.CenterHorizontally))
            LogOutButton(Modifier.align(Alignment.CenterHorizontally)) {
                coroutineScope.launch {
                    viewModel.logout()
                }
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier) {
    Text(
        text = "Mi Perfil",
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun ProfileImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.baseline_face_24),
        contentDescription = "Foto de perfil",
        contentScale = ContentScale.Crop,
        modifier = modifier.size(125.dp)
    )
}

@Composable
fun LogOutButton(modifier: Modifier, onLogoutSelected: () -> Unit) {
    Button(
        onClick = {
            onLogoutSelected()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = RojoOscuro,
            contentColor = Color.White
        )
    ) {
        Text(text = "Cerrar sesión")
    }
}