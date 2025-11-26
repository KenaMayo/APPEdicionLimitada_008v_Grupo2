package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vivitasol.carcasamvvm.viewmodels.ClienteViewModel

@Composable
fun ClienteListView(vm: ClienteViewModel = viewModel(), navController: NavController) {
    val clientes by vm.clientes.collectAsState()

    if (clientes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(clientes) { cliente ->
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { navController.navigate("clienteDetail/${cliente.id}") },
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = cliente.nombre,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = cliente.email)
                    Text(text = "${cliente.comuna}, ${cliente.region}")
                }
                Divider()
            }
        }
    }
}
