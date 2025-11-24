package com.vivitasol.carcasamvvm.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vivitasol.carcasamvvm.R
import com.vivitasol.carcasamvvm.navigation.Route
import com.vivitasol.carcasamvvm.viewmodels.RegisterViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(navController: NavController, vm: RegisterViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    val errors by vm.errors.collectAsState()
    val registrationSuccess by vm.registrationSuccess.collectAsState()

    var regionExpanded by remember { mutableStateOf(false) }
    var comunaExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            delay(2000) // Espera 2 segundos
            navController.navigate(Route.Login.route) {
                popUpTo(Route.Register.route) { inclusive = true }
            }
            vm.registrationShown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Black
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Image(painter = painterResource(id = R.drawable.logoedicionlimitadapng), contentDescription = "Logo", modifier = Modifier.size(150.dp)) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { Text("Formulario de registro", style = MaterialTheme.typography.headlineLarge, color = Color.White) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { OutlinedTextField(value = state.name, onValueChange = vm::onNameChange, label = { Text("Nombre") }, isError = errors.name != null, supportingText = { if (errors.name != null) Text(errors.name!!) }, modifier = Modifier.fillMaxWidth()) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { OutlinedTextField(value = state.email, onValueChange = vm::onEmailChange, label = { Text("Email") }, isError = errors.email != null, supportingText = { if (errors.email != null) Text(errors.email!!) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth()) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }) {
                        OutlinedTextField(
                            value = state.region,
                            onValueChange = {}, // No-op
                            readOnly = true,
                            label = { Text("Región") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            isError = errors.region != null,
                            supportingText = { if (errors.region != null) Text(errors.region!!) }
                        )
                        ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                            vm.regions.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region) },
                                    onClick = {
                                        vm.onRegionChange(region)
                                        regionExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    ExposedDropdownMenuBox(expanded = comunaExpanded, onExpandedChange = { comunaExpanded = !comunaExpanded }) {
                        OutlinedTextField(
                            value = state.comuna,
                            onValueChange = {}, // No-op
                            readOnly = true,
                            label = { Text("Comuna") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            isError = errors.comuna != null,
                            supportingText = { if (errors.comuna != null) Text(errors.comuna!!) }
                        )
                        if (state.region.isNotBlank()) {
                            ExposedDropdownMenu(expanded = comunaExpanded, onDismissRequest = { comunaExpanded = false }) {
                                vm.comunasByRegion[state.region]?.forEach { comuna ->
                                    DropdownMenuItem(
                                        text = { Text(comuna) },
                                        onClick = {
                                            vm.onComunaChange(comuna)
                                            comunaExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { OutlinedTextField(value = state.password, onValueChange = vm::onPasswordChange, label = { Text("Contraseña") }, isError = errors.password != null, supportingText = { if (errors.password != null) Text(errors.password!!) }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth()) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { OutlinedTextField(value = state.confirmPassword, onValueChange = vm::onConfirmPasswordChange, label = { Text("Reescriba la contraseña") }, isError = errors.confirmPassword != null, supportingText = { if (errors.confirmPassword != null) Text(errors.confirmPassword!!) }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth()) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { Button(onClick = { vm.register() }, modifier = Modifier.fillMaxWidth()) { Text("Registrarse") } }
            }
        }

        AnimatedVisibility(visible = registrationSuccess, enter = slideInVertically(initialOffsetY = { it }) + fadeIn(), exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(), modifier = Modifier.align(Alignment.Center)) {
            Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                Text(text = "Registro exitoso", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 48.dp, vertical = 32.dp))
            }
        }
    }
}
