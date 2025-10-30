package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.carcasamvvm.model.Product
import com.vivitasol.carcasamvvm.viewmodels.DetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleView(
    vm: DetailViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        if (state.selectedProduct == null) {
            ProductList(paddingValues = it, products = state.products, onProductClick = vm::onProductSelected)
        } else {
            ProductDetail(paddingValues = it, product = state.selectedProduct!!, vm = vm) {
                vm.onProductEdited()
                scope.launch {
                    snackbarHostState.showSnackbar("Producto editado")
                }
            }
        }
    }
}

@Composable
fun ProductList(paddingValues: PaddingValues, products: List<Product>, onProductClick: (Product) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(
            "Productos Agregados Recientemente",
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
                        Text(product.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.weight(1f))
                        Text("Ver detalle", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetail(
    paddingValues: PaddingValues, 
    product: Product, 
    vm: DetailViewModel, 
    onProductEdited: () -> Unit
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
    }
}
