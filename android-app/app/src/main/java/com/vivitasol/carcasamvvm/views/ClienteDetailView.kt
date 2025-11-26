package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vivitasol.carcasamvvm.model.Cliente
import com.vivitasol.carcasamvvm.viewmodels.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteDetailView(
    paddingValues: PaddingValues,
    cliente: Cliente,
    vm: ClienteViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Editar Cliente", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = cliente.nombre,
            onValueChange = vm::onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cliente.email,
            onValueChange = vm::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cliente.comuna,
            onValueChange = vm::onComunaChange,
            label = { Text("Comuna") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cliente.region,
            onValueChange = vm::onRegionChange,
            label = { Text("Región") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { vm.updateCliente(cliente) }) {
                Text("Actualizar")
            }
            OutlinedButton(onClick = { vm.clearSelectedCliente() }) {
                Text("Cancelar")
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { vm.deleteCliente(cliente) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Eliminar Cliente")
        }
    }
}
