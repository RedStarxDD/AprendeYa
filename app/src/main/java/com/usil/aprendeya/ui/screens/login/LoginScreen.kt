package com.usil.aprendeya.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.usil.aprendeya.R
import com.usil.aprendeya.ui.theme.Boton
import com.usil.aprendeya.ui.theme.VerdeOscuro
import com.usil.aprendeya.viewModel.login.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LaunchedEffect(Unit) {
            viewModel.event.collect {}
        }
        HeaderImg()
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Login(Modifier.align(Alignment.Center), viewModel)
        }
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel) {
    val email: String by viewModel.email.collectAsState()
    val password: String by viewModel.password.collectAsState()
    val loginEnabled: Boolean by viewModel.loginEnabled.collectAsState()
    val isLoading: Boolean by viewModel.isLoading.collectAsState()
    val loginError: String by viewModel.loginError.collectAsState()
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
                text = "Iniciando sesión",
                color = Boton,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Column(modifier = modifier) {
            Title(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.padding(16.dp))
            LogoImg(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email) { viewModel.onLoginChanged(it, password) }
            Spacer(modifier = Modifier.padding(8.dp))
            PasswordField(password) { viewModel.onLoginChanged(email, it) }
            ErrorMessage(loginError)
            Spacer(modifier = Modifier.padding(4.dp))
            ForgotPassword(Modifier.align(Alignment.End))
            Spacer(modifier = Modifier.padding(16.dp))
            LoginButton(Modifier.align(Alignment.CenterHorizontally), loginEnabled) {
                coroutineScope.launch {
                    viewModel.onLoginSelected()
                }
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier) {
    Text(
        text = "Iniciar sesión",
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun LoginButton(modifier: Modifier, loginEnabled: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = modifier
            .width(200.dp)
            .height((48.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = VerdeOscuro,
            disabledContainerColor = VerdeOscuro.copy(alpha = 0.25f),
            contentColor = Color.White,
            disabledContentColor = Color.White,
        ),
        enabled = loginEnabled
    ) {
        Text(text = "Iniciar sesión")
    }
}

@Composable
fun ErrorMessage(errorMessage: String? = null) {
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            fontSize = 12.sp,
            color = Color.Red
        )
    }
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "¿Olvidaste la contraseña?",
        modifier = modifier.clickable { throw RuntimeException("Falla") },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Red
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    Column {
        Text(
            text = "Contraseña:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TextField(
            value = password,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Ingrese su contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(focusedTextColor = Color.Black)
        )
    }
}

@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    Column {
        Text(
            text = "Correo electrónico:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TextField(
            value = email,
            onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Ingrese su correo")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(focusedTextColor = Color.Black)
        )
    }
}

@Composable
fun LogoImg(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        modifier = modifier.size(125.dp)
    )
}

@Composable
fun HeaderImg() {
    Image(
        painter = painterResource(id = R.drawable.portada_login),
        contentDescription = "Encabezado",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
    )
}
