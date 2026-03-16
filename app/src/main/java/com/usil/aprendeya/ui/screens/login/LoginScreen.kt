package com.usil.aprendeya.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.usil.aprendeya.R
import com.usil.aprendeya.ui.screens.components.HeaderImg
import com.usil.aprendeya.ui.screens.components.LoadingCircle
import com.usil.aprendeya.ui.theme.VerdeOscuro
import com.usil.aprendeya.viewModel.login.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
    val blockVersion by viewModel.blockVersion.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (blockVersion) {
        OldVersionAlert {
            viewModel.closeOldVersionDialog()
        }
    }
    if (isLoading) {
        LoadingCircle("Iniciando sesión")
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
            /*Spacer(modifier = Modifier.padding(4.dp))
            ForgotPassword(Modifier.align(Alignment.End))*/
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
fun OldVersionAlert(onDismissRequest: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = "Actualización necesaria")
        },
        text = {
            Text(text = "Usted está usando una versión antigua de la app, por favor actualice a la última versión para la mejor experiencia")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text("Entendido")
            }
        }
    )
}

@Composable
fun Title(modifier: Modifier) {
    Text(
        text = "Iniciar sesión",
        style = MaterialTheme.typography.headlineMedium,
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
            .height(48.dp),
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
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red
        )
    }
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "¿Olvidaste la contraseña?",
        modifier = modifier.clickable { throw RuntimeException("Falla") },
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = Color.Red
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    Column {
        Text(
            text = "Contraseña:",
            style = MaterialTheme.typography.titleMedium,
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
            style = MaterialTheme.typography.titleMedium,
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