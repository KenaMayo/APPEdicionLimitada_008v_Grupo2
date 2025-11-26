package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.model.Cliente
import com.vivitasol.carcasamvvm.model.ClienteRequest
import com.vivitasol.carcasamvvm.remote.ApiClient
import com.vivitasol.carcasamvvm.remote.ClienteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClienteViewModel : ViewModel() {

    private val _clientes = MutableStateFlow<List<Cliente>>(emptyList())
    val clientes = _clientes.asStateFlow()

    private val _clienteCreado = MutableStateFlow(false)
    val clienteCreado = _clienteCreado.asStateFlow()

    private val clienteService = ApiClient.retrofit.create(ClienteService::class.java)

    init {
        getClientes()
    }

    private fun getClientes() {
        viewModelScope.launch {
            try {
                _clientes.value = clienteService.getClientes()
            } catch (e: Exception) {
                // Manejar el error, por ejemplo, loggeando o mostrando un mensaje
            }
        }
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
                // ¡CORREGIDO! La función en el service se llama 'register'.
                val response = clienteService.register(request)
                if (response.isSuccessful) {
                    // Si la llamada es exitosa, actualizamos el estado
                    _clienteCreado.value = true
                    // y refrescamos la lista de clientes
                    getClientes()
                }
            } catch (e: Exception) {
                // Manejar el error
                _clienteCreado.value = false
            }
        }
    }

    fun onClienteCreadoHandled() {
        _clienteCreado.value = false
    }
}
