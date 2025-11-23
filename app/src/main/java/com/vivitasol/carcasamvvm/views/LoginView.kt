package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.viewmodels.LoginResult
import com.vivitasol.carcasamvvm.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    navController: NavController,
    vm: LoginViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val errors by vm.errors.collectAsState()
    val loginResult by vm.loginResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(loginResult) {
        when (loginResult) {
            is LoginResult.AdminSuccess -> {
                navController.navigate(Route.MenuShell.route) {
                    popUpTo(Route.Login.route) { inclusive = true }
                }
                vm.resetLoginResult()
            }
            is LoginResult.UserSuccess -> {
                navController.navigate(Route.UserMenuShell.route) {
                    popUpTo(Route.Login.route) { inclusive = true }
                }
                vm.resetLoginResult()
            }
            is LoginResult.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(errors.general ?: "Error desconocido")
                }
                vm.resetLoginResult()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Edicion Limitada", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logoedicionlimitadapng),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = vm::onEmailChange,
                label = { Text("Correo") },
                isError = errors.email != null,
                supportingText = { if (errors.email != null) Text(errors.email!!) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.pass,
                onValueChange = vm::onPassChange,
                label = { Text("Contraseña") },
                isError = errors.pass != null,
                supportingText = { if (errors.pass != null) Text(errors.pass!!) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { vm.login() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate(Route.Register.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
    }
}
