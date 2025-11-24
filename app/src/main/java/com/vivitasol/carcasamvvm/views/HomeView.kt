package com.vivitasol.carcasamvvm.views

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivitasol.carcasamvvm.LimitedEditionApp
import com.vivitasol.carcasamvvm.model.Product
import com.vivitasol.carcasamvvm.viewmodels.HomeViewModel
import com.vivitasol.carcasamvvm.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    val activity = LocalContext.current as Activity
    val vm: HomeViewModel = viewModel(factory = ViewModelFactory((activity.application as LimitedEditionApp).repository))

    val products by vm.products.collectAsState()
    var showAnimatedMessage by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    if (showAnimatedMessage) {
        LaunchedEffect(key1 = true) {
            delay(1000)
            showAnimatedMessage = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = { selectedProduct = product },
                        onAddToCart = { showAnimatedMessage = true }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = selectedProduct != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            selectedProduct?.let { product ->
                ProductDetailPopup(
                    product = product,
                    onDismiss = { selectedProduct = null },
                    onAddToCart = {
                        selectedProduct = null
                        showAnimatedMessage = true
                    }
                )
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
                    text = "Producto agregado",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 48.dp, vertical = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onProductClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(8.dp))
            )
            Text(text = product.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "by ${product.designer}", style = MaterialTheme.typography.bodyLarge)
            Text(text = formatPrice(product.price), style = MaterialTheme.typography.headlineSmall)
            Text(text = "Stock: ${product.stock}", style = MaterialTheme.typography.labelMedium)
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(),
                enabled = product.stock > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Agregar a Carrito")
            }
        }
    }
}

@Composable
private fun ProductDetailPopup(
    product: Product,
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable( // Clic fuera de la tarjeta para cerrar
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .clickable(enabled = false, onClick = {}), // Evita que el clic se propague al fondo
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(12.dp))
                )
                Text(text = product.name, style = MaterialTheme.typography.titleLarge)
                Text(text = "by ${product.designer}", style = MaterialTheme.typography.bodyLarge)
                Text(text = formatPrice(product.price), style = MaterialTheme.typography.headlineSmall)
                Text(text = "Stock: ${product.stock}", style = MaterialTheme.typography.labelMedium)
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = product.stock > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Agregar a Carrito")
                }
            }
        }
    }
}
