package com.vivitasol.carcasamvvm.views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
fun CreateProductView() {
    val activity = LocalContext.current as Activity
    val vm: CreateProductViewModel = viewModel(factory = ViewModelFactory((activity.application as LimitedEditionApp).repository))

    val state = vm.state.collectAsState().value
    val errors = vm.errors.collectAsState().value
    var showAnimatedMessage by remember { mutableStateOf(false) }

    var hasCameraPermission by rememberSaveable { mutableStateOf(false) }
    var pendingImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    if (showAnimatedMessage) {
        LaunchedEffect(key1 = true) {
            delay(1000)
            showAnimatedMessage = false
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            vm.onImageChange(pendingImageUri)
        } else {
            vm.onImageChange(null)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (granted) {
            launchCamera(activity) { tempUri ->
                pendingImageUri = tempUri
                takePictureLauncher.launch(tempUri)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold {
            padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Crear Nuevo Producto", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(
                    value = state.name,
                    onValueChange = vm::onNameChange,
                    label = { Text("Nombre del producto") },
                    isError = errors.name != null,
                    supportingText = { if (errors.name != null) Text(errors.name!!) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.designer,
                    onValueChange = vm::onDesignerChange,
                    label = { Text("Diseñador") },
                    isError = errors.designer != null,
                    supportingText = { if (errors.designer != null) Text(errors.designer!!) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.price,
                    onValueChange = vm::onPriceChange,
                    label = { Text("Precio") },
                    isError = errors.price != null,
                    supportingText = { if (errors.price != null) Text(errors.price!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.stock,
                    onValueChange = vm::onStockChange,
                    label = { Text("Stock") },
                    isError = errors.stock != null,
                    supportingText = { if (errors.stock != null) Text(errors.stock!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 180.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    if (state.imageUri != null) {
                        AsyncImage(
                            model = state.imageUri,
                            contentDescription = "Foto del producto",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Aún no hay foto")
                        }
                    }
                }
                if (errors.image != null) {
                    Text(errors.image!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (hasCameraPermission) {
                                launchCamera(activity) { tempUri ->
                                    pendingImageUri = tempUri
                                    takePictureLauncher.launch(tempUri)
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    ) {
                        Text("Tomar Foto")
                    }
                    Button(
                        onClick = {
                            vm.createProduct()
                            showAnimatedMessage = true
                        }
                    ) {
                        Text("Agregar producto", fontSize = 12.sp)
                    }
                }
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
                    text = "Producto creado",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 48.dp, vertical = 32.dp)
                )
            }
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
        "IMG_${timeStamp}_",
        ".jpg",
        storageDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        image
    )
}

private fun launchCamera(context: Context, onUriReady: (Uri) -> Unit) {
    val uri = createImageUri(context)
    onUriReady(uri)
}
