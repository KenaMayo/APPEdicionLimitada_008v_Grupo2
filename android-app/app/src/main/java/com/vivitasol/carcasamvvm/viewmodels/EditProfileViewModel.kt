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
    val id: String? = null,
    val nombre: String = "",
    val email: String = "",
    val region: String = "",
    val comuna: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val message: String? = null,
    val saveSuccess: Boolean = false
)

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val clienteService = ApiClient.retrofit.create(ClienteService::class.java)

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()

    val regions = listOf("Metropolitana", "Valparaíso", "Bíobío", "Maule", "Los Lagos", "La Araucanía")
    val comunasByRegion = mapOf(
        "Metropolitana" to listOf("Santiago", "Providencia", "Las Condes", "Ñuñoa", "Maipú", "Puente Alto", "La Florida", "Vitacura"),
        "Valparaíso" to listOf("Valparaíso", "Viña del Mar", "Quilpué", "Villa Alemana", "Quillota"),
        "Bíobío" to listOf("Concepción", "Talcahuano", "Hualpén", "Coronel", "Los Ángeles"),
        "Maule" to listOf("Talca", "Curicó", "Linares", "Cauquenes"),
        "Los Lagos" to listOf("Puerto Montt", "Osorno", "Puerto Varas", "Castro"),
        "La Araucanía" to listOf("Temuco", "Padre Las Casas", "Villarrica", "Angol")
    )

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
                        response.body()?.let {
                            _state.update {
                                currentState -> currentState.copy(
                                    id = it.id,
                                    nombre = it.nombre,
                                    email = it.email,
                                    region = it.region,
                                    comuna = it.comuna
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    _state.update { it.copy(message = "Error al cargar el perfil") }
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _state.update { it.copy(nombre = name) }
    }

    fun onRegionChange(region: String) {
        _state.update { it.copy(region = region, comuna = "") } 
    }

    fun onComunaChange(comuna: String) {
        _state.update { it.copy(comuna = comuna) }
    }
    
    fun onPasswordChange(password: String) {
        _state.update { it.copy(newPassword = password) }
    }

    fun onConfirmPasswordChange(password: String) {
        _state.update { it.copy(confirmNewPassword = password) }
    }

    fun saveChanges() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState.id == null) return@launch

            if (currentState.newPassword.isNotBlank() || currentState.confirmNewPassword.isNotBlank()) {
                if (currentState.newPassword != currentState.confirmNewPassword) {
                    _state.update { it.copy(message = "Las contraseñas no coinciden") }
                    return@launch
                }
            }

            val clienteToUpdate = Cliente(
                id = currentState.id,
                nombre = currentState.nombre,
                email = currentState.email,
                region = currentState.region,
                comuna = currentState.comuna,
                contrasena = if (currentState.newPassword.isNotBlank()) currentState.newPassword else null
            )

            try {
                val response = clienteService.updateCliente(currentState.id, clienteToUpdate)
                if (response.isSuccessful) {
                    _state.update { it.copy(message = "Perfil actualizado con éxito", saveSuccess = true) }
                } else {
                    _state.update { it.copy(message = "Error al actualizar el perfil") }
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
