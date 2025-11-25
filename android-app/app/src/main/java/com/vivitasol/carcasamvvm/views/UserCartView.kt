package com.vivitasol.carcasamvvm.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vivitasol.carcasamvvm.viewmodels.CartViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCartView(vm: CartViewModel) {
    val state by vm.state.collectAsState()

    LaunchedEffect(state.showPurchaseSuccess) {
        if (state.showPurchaseSuccess) {
            delay(2000) // Espera 2 segundos
            vm.purchaseMessageShown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold {
            if (state.items.isEmpty() && !state.showPurchaseSuccess) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("El carrito está vacío")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(16.dp)
                ) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(state.items) { cartItem ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${cartItem.product.name} (x${cartItem.quantity})")
                                Text(text = formatPrice(cartItem.product.price * cartItem.quantity))
                            }
                            Divider()
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(formatPrice(state.total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { vm.simulatePurchase() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.items.isNotEmpty()
                    ) {
                        Text("Realizar compra")
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = state.showPurchaseSuccess,
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
                    text = "Pago exitoso",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 48.dp, vertical = 32.dp)
                )
            }
        }
    }
}
