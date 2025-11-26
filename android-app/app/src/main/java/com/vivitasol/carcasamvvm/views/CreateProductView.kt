package com.vivitasol.carcasamvvm.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.vivitasol.carcasamvvm.LimitedEditionApp
import com.vivitasol.carcasamvvm.viewmodels.CreateProductViewModel
import com.vivitasol.carcasamvvm.viewmodels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductView() {
    val activity = LocalContext.current as Activity
    val application = activity.application as LimitedEditionApp
    val vm: CreateProductViewModel = viewModel(factory = ViewModelFactory(application, application.repository, application.clientRepository))
    val state by vm.state.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.data?.let { uri ->
                // Otorga permisos persistentes de lectura para el URI
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                activity.contentResolver.takePersistableUriPermission(uri, takeFlags)
                vm.onImageSelected(uri)
            }
        }
    }

    Scaffold {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
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
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                    }
                    imagePickerLauncher.launch(intent)
                }) {
                    Text("Seleccionar Imagen")
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
        }
    }
}
