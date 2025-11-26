package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.PrefsRepo
import com.vivitasol.carcasamvvm.model.Cliente
import com.vivitasol.carcasamvvm.remote.ApiClient
import com.vivitasol.carcasamvvm.remote.ClienteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileState(
    val cliente: Cliente? = null,
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val message: String? = null,
    val saveSuccess: Boolean = false
)

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val clienteService = ApiClient.retrofit.create(ClienteService::class.java)

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()

    // NOTA: La región y comuna ya no están en el modelo Cliente de la API.
    // Si necesitas estos campos, debes añadirlos a tu modelo en Spring Boot
    // y a la data class Cliente en Android.

    init {
        loadCliente(application)
    }

    private fun loadCliente(application: Application) {
        viewModelScope.launch {
            val email = PrefsRepo.getEmail(application).first()
            if (email != null) {
                try {
                    val response = clienteService.findByEmail(email)
                    if (response.isSuccessful) {
                        _state.update { it.copy(cliente = response.body()) }
                    }
                } catch (e: Exception) {
                    _state.update { it.copy(message = "Error al cargar el perfil") }
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _state.update { it.copy(cliente = it.cliente?.copy(nombre = name)) }
    }

    // Si necesitas editar la edad, añade aquí una función onAgeChange

    fun onPasswordChange(password: String) {
        _state.update { it.copy(newPassword = password) }
    }

    fun onConfirmPasswordChange(password: String) {
        _state.update { it.copy(confirmNewPassword = password) }
    }

    fun saveChanges() {
        viewModelScope.launch {
            val currentCliente = _state.value.cliente ?: return@launch

            // TODO: Añadir validación de contraseña si es necesario
            // var clienteToUpdate = currentCliente.copy()
            // if(_state.value.newPassword.isNotBlank()) {
            //     clienteToUpdate = clienteToUpdate.copy(contrasena = _state.value.newPassword)
            // }

            try {
                currentCliente.id?.let {
                    val response = clienteService.updateCliente(it, currentCliente)
                    if (response.isSuccessful) {
                        _state.update { it.copy(message = "Perfil actualizado con éxito", saveSuccess = true) }
                    } else {
                        _state.update { it.copy(message = "Error al actualizar el perfil") }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(message = "Error de red: ${e.message}") }
            }
        }
    }

    fun messageShown() {
        _state.update { it.copy(message = null, saveSuccess = false) }
    }
}
