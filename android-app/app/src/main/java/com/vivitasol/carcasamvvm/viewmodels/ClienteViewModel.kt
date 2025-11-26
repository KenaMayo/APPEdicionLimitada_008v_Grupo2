package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.model.Cliente
import com.vivitasol.carcasamvvm.model.ClienteRequest
import com.vivitasol.carcasamvvm.remote.ApiClient
import com.vivitasol.carcasamvvm.remote.ClienteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClienteState(
    val clientes: List<Cliente> = emptyList(),
    val selectedCliente: Cliente? = null,
    val clienteCreado: Boolean = false,
    val clienteActualizado: Boolean = false,
    val clienteEliminado: Boolean = false,
    val message: String? = null
)

class ClienteViewModel : ViewModel() {

    private val _state = MutableStateFlow(ClienteState())
    val state = _state.asStateFlow()

    private val clienteService = ApiClient.retrofit.create(ClienteService::class.java)

    init {
        getClientes()
    }

    fun getClientes() {
        viewModelScope.launch {
            try {
                val clientes = clienteService.getClientes()
                _state.update { it.copy(clientes = clientes) }
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error al cargar los clientes: ${e.message}") }
            }
        }
    }

    fun onClienteSelected(cliente: Cliente) {
        _state.update { it.copy(selectedCliente = cliente) }
    }

    fun clearSelectedCliente() {
        _state.update { it.copy(selectedCliente = null) }
    }

    fun createCliente(nombre: String, email: String, comuna: String, region: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val request = ClienteRequest(
                    nombre = nombre,
                    email = email,
                    comuna = comuna,
                    region = region,
                    contrasena = contrasena
                )
                val response = clienteService.register(request)
                if (response.isSuccessful) {
                    _state.update { it.copy(clienteCreado = true) }
                    getClientes()
                } else {
                    _state.update { it.copy(clienteCreado = false, message = "Error al crear el cliente. Código: ${response.code()}") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(clienteCreado = false, message = "Error de red al crear: ${e.message}") }
            }
        }
    }

    fun updateCliente(cliente: Cliente) {
        if (cliente.id == null) {
            _state.update { it.copy(message = "ID de cliente no encontrado. No se puede actualizar.") }
            return
        }
        viewModelScope.launch {
            try {
                val response = clienteService.updateCliente(cliente.id, cliente)
                if (response.isSuccessful) {
                    _state.update { it.copy(clienteActualizado = true, selectedCliente = null) }
                    getClientes()
                } else {
                    _state.update { it.copy(clienteActualizado = false, message = "Error al actualizar el cliente. Código: ${response.code()}") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(clienteActualizado = false, message = "Error de red al actualizar: ${e.message}") }
            }
        }
    }

    fun deleteCliente(cliente: Cliente) {
        if (cliente.id == null) {
            _state.update { it.copy(message = "ID de cliente no encontrado. No se puede eliminar.") }
            return
        }
        viewModelScope.launch {
            try {
                val response = clienteService.deleteCliente(cliente.id)
                if (response.isSuccessful) {
                    _state.update { it.copy(clienteEliminado = true, selectedCliente = null) }
                    getClientes()
                } else {
                    _state.update { it.copy(clienteEliminado = false, message = "Error al eliminar el cliente. Código: ${response.code()}") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(clienteEliminado = false, message = "Error de red al eliminar: ${e.message}") }
            }
        }
    }

    fun onClienteCreadoHandled() {
        _state.update { it.copy(clienteCreado = false) }
    }

    fun onClienteActualizadoHandled() {
        _state.update { it.copy(clienteActualizado = false) }
    }

    fun onClienteEliminadoHandled() {
        _state.update { it.copy(clienteEliminado = false) }
    }

    fun onMessageHandled() {
        _state.update { it.copy(message = null) }
    }

    fun onNombreChange(nombre: String) {
        _state.update { it.copy(selectedCliente = it.selectedCliente?.copy(nombre = nombre)) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(selectedCliente = it.selectedCliente?.copy(email = email)) }
    }

    fun onComunaChange(comuna: String) {
        _state.update { it.copy(selectedCliente = it.selectedCliente?.copy(comuna = comuna)) }
    }

    fun onRegionChange(region: String) {
        _state.update { it.copy(selectedCliente = it.selectedCliente?.copy(region = region)) }
    }
}
