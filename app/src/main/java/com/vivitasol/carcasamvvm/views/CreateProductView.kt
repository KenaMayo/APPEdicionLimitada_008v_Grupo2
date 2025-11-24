package com.vivitasol.carcasamvvm.views

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.vivitasol.carcasamvvm.LimitedEditionApp
import com.vivitasol.carcasamvvm.viewmodels.CreateProductViewModel
import com.vivitasol.carcasamvvm.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateProductView() {
    val activity = LocalContext.current as Activity
    val vm: CreateProductViewModel = viewModel(factory = ViewModelFactory(activity.application, (activity.application as LimitedEditionApp).repository))

    val state by vm.state.collectAsState()
    val errors by vm.errors.collectAsState()
    var showAnimatedMessage by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (showAnimatedMessage) {
        LaunchedEffect(key1 = true) {
            delay(1000)
            showAnimatedMessage = false
            vm.reset()
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            vm.onImageChange(imageUri)
        } else {
            vm.onImageChange(null)
        }
    }

    fun launchCamera(context: Context) {
        imageUri = createImageUri(context)
        takePictureLauncher.launch(imageUri!!)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold {
            padding ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Crear Nuevo Producto", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(value = state.name, onValueChange = vm::onNameChange, label = { Text("Nombre del producto") }, isError = errors.name != null, supportingText = { if (errors.name != null) Text(errors.name!!) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.designer, onValueChange = vm::onDesignerChange, label = { Text("Diseñador") }, isError = errors.designer != null, supportingText = { if (errors.designer != null) Text(errors.designer!!) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.price, onValueChange = vm::onPriceChange, label = { Text("Precio") }, isError = errors.price != null, supportingText = { if (errors.price != null) Text(errors.price!!) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.stock, onValueChange = vm::onStockChange, label = { Text("Stock") }, isError = errors.stock != null, supportingText = { if (errors.stock != null) Text(errors.stock!!) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 180.dp)
                        .clickable {
                            if (cameraPermissionState.status.isGranted) {
                                launchCamera(activity)
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (state.imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(model = state.imageUri),
                                contentDescription = "Foto del producto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text("Toca para tomar una foto")
                        }
                    }
                }
                if (errors.image != null) {
                    Text(errors.image!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = {
                        vm.createProduct()
                        if(vm.validate()) showAnimatedMessage = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar producto", fontSize = 12.sp)
                }
            }
        }

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
