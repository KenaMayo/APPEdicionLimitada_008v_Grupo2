package com.vivitasol.carcasamvvm.views

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.carcasamvvm.LimitedEditionApp
import com.vivitasol.carcasamvvm.model.Product
import com.vivitasol.carcasamvvm.viewmodels.DetailViewModel
import com.vivitasol.carcasamvvm.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView() {
    val activity = LocalContext.current as Activity
    val application = activity.application as LimitedEditionApp
    val vm: DetailViewModel = viewModel(factory = ViewModelFactory(application, application.repository, application.clientRepository))

    val state by vm.state.collectAsState()
    val allProducts by vm.allProducts.collectAsState()
    var showAnimatedMessage by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    if (showAnimatedMessage) {
        LaunchedEffect(key1 = true) {
            delay(1000) // Duración del mensaje en pantalla
            showAnimatedMessage = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold {
            padding ->
            if (state.selectedProduct == null) {
                ProductListView(paddingValues = padding, products = allProducts, onProductClick = vm::onProductSelected)
            } else {
                ProductDetailView(
                    paddingValues = padding, 
                    product = state.selectedProduct!!, 
                    vm = vm, 
                    onProductEdited = {
                        vm.onProductEdited()
                        message = "Producto editado correctamente"
                        showAnimatedMessage = true
                    },
                    onProductDeleted = {
                        vm.deleteProduct()
                        message = "Producto eliminado correctamente"
                        showAnimatedMessage = true
                    }
                )
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

@Composable
fun ProductListView(paddingValues: PaddingValues, products: List<Product>, onProductClick: (Product) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(
            "Productos Agregados",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(products) { product ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(product.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
                        Spacer(Modifier.weight(1f))
                        Text("Ver detalles", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailView(
    paddingValues: PaddingValues,
    product: Product,
    vm: DetailViewModel,
    onProductEdited: () -> Unit,
    onProductDeleted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Editar Producto", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = product.name,
            onValueChange = vm::onNameChange,
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = product.designer,
            onValueChange = vm::onDesignerChange,
            label = { Text("Diseñador") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = product.price.toString(),
            onValueChange = vm::onPriceChange,
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = product.stock.toString(),
            onValueChange = vm::onStockChange,
            label = { Text("Stock") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onProductEdited) {
                Text("Editar producto")
            }
            OutlinedButton(onClick = { vm.clearSelectedProduct() }) {
                Text("Cancelar")
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onProductDeleted,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Eliminar producto")
        }
    }
}
