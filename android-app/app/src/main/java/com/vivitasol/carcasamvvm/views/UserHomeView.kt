package com.vivitasol.carcasamvvm.views

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.vivitasol.carcasamvvm.LimitedEditionApp
import com.vivitasol.carcasamvvm.model.Product
import com.vivitasol.carcasamvvm.utils.formatPrice
import com.vivitasol.carcasamvvm.viewmodels.CartViewModel
import com.vivitasol.carcasamvvm.viewmodels.UserHomeViewModel
import com.vivitasol.carcasamvvm.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeView(cartViewModel: CartViewModel) {
    val activity = LocalContext.current as Activity
    val application = activity.application as LimitedEditionApp
    val vm: UserHomeViewModel = viewModel(factory = ViewModelFactory(application, application.repository, application.clientRepository))
    val cartState by cartViewModel.state.collectAsState()

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
                    val cartItem = cartState.items.find { it.product.id == product.id }
                    val quantityInCart = cartItem?.quantity ?: 0

                    ProductCard(
                        product = product,
                        quantityInCart = quantityInCart,
                        onProductClick = { selectedProduct = product },
                        onAddToCart = {
                            if (cartViewModel.addProduct(product)) {
                                showAnimatedMessage = true
                            }
                        }
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
                val cartItem = cartState.items.find { it.product.id == product.id }
                val quantityInCart = cartItem?.quantity ?: 0

                ProductDetailPopup(
                    product = product,
                    quantityInCart = quantityInCart,
                    onDismiss = { selectedProduct = null },
                    onAddToCart = {
                        if (cartViewModel.addProduct(product)) {
                            showAnimatedMessage = true
                        }
                        selectedProduct = null
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
                    text = "Producto agregado al carrito",
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
    quantityInCart: Int,
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
                painter = rememberAsyncImagePainter(model = product.imageUri),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(8.dp))
            )
            Text(text = product.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "by ${product.designer}", style = MaterialTheme.typography.bodyLarge)
            Text(text = formatPrice(product.price), style = MaterialTheme.typography.headlineSmall)
            Text(text = "Stock: ${product.stock - quantityInCart}", style = MaterialTheme.typography.labelMedium)
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(),
                enabled = product.stock > quantityInCart,
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
    quantityInCart: Int,
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .clickable(enabled = false, onClick = {}),
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
                    painter = rememberAsyncImagePainter(model = product.imageUri),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(12.dp))
                )
                Text(text = product.name, style = MaterialTheme.typography.titleLarge)
                Text(text = "by ${product.designer}", style = MaterialTheme.typography.bodyLarge)
                Text(text = formatPrice(product.price), style = MaterialTheme.typography.headlineSmall)
                Text(text = "Stock: ${product.stock - quantityInCart}", style = MaterialTheme.typography.labelMedium)
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = product.stock > quantityInCart,
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
