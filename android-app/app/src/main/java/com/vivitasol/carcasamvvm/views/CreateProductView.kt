package com.vivitasol.carcasamvvm.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vivitasol.carcasamvvm.LimitedEditionApp
import com.vivitasol.carcasamvvm.viewmodels.CreateProductViewModel
import com.vivitasol.carcasamvvm.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductView(navController: NavController) {
    val activity = LocalContext.current as Activity
    val application = activity.application as LimitedEditionApp
    val vm: CreateProductViewModel = viewModel(factory = ViewModelFactory(application, application.repository, application.clientRepository))
    val state by vm.state.collectAsState()
    val context = LocalContext.current

    var showAnimatedMessage by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(state.saveSuccess, state.message) {
        if (state.saveSuccess) {
            message = state.message ?: "Producto guardado"
            showAnimatedMessage = true
            delay(1200) // Espera a que la animación sea visible
            navController.popBackStack()
            vm.onSaveHandled()
        } else if (state.message != null) {
            message = state.message!!
            showAnimatedMessage = true
        }
    }

    if (showAnimatedMessage) {
        LaunchedEffect(key1 = true) {
            delay(1500) // Duración total del mensaje en pantalla
            showAnimatedMessage = false
            vm.onSaveHandled() // Limpia el mensaje de error también
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.data?.let {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(it, takeFlags)
                vm.onImageSelected(it)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            vm.onImageSelected(vm.latestTmpUri.value)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val tmpUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", File.createTempFile("JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}", ".jpg", context.externalCacheDir))
            vm.onTmpUriReady(tmpUri)
            cameraLauncher.launch(tmpUri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                state.imageUri?.let {
                    Image(painter = rememberAsyncImagePainter(it), contentDescription = "Selected image", modifier = Modifier.height(150.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "image/*"
                        }
                        imagePickerLauncher.launch(intent)
                    }) {
                        Text("Seleccionar Imagen")
                    }
                    Button(onClick = { cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA) }) {
                        Text("Tomar Foto")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = state.name, onValueChange = vm::onNameChange, label = { Text("Nombre del producto") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.designer, onValueChange = vm::onDesignerChange, label = { Text("Diseñador") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.price, onValueChange = vm::onPriceChange, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.stock, onValueChange = vm::onStockChange, label = { Text("Stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { vm.saveProduct() }, modifier = Modifier.fillMaxWidth(), enabled = state.imageUri != null) {
                    Text("Guardar Producto")
                }
            }

            // Mensaje animado que aparece en el centro
            AnimatedVisibility(
                visible = showAnimatedMessage,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 48.dp, vertical = 32.dp)
                    )
                }
            }
        }
    }
}
