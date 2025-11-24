package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.AppDatabase
import com.vivitasol.carcasamvvm.data.PrefsRepo
import com.vivitasol.carcasamvvm.model.Cliente
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
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()

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
        viewModelScope.launch {
            val email = PrefsRepo.getEmail(application).first()
            if (email != null) {
                val cliente = clienteDao.findByEmail(email)
                _state.update { it.copy(cliente = cliente) }
            }
        }
    }

    fun onNameChange(name: String) {
        _state.update { it.copy(cliente = it.cliente?.copy(nombre = name)) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(cliente = it.cliente?.copy(email = email)) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(newPassword = password) }
    }

    fun onConfirmPasswordChange(password: String) {
        _state.update { it.copy(confirmNewPassword = password) }
    }

    fun onComunaChange(comuna: String) {
        _state.update { it.copy(cliente = it.cliente?.copy(comuna = comuna)) }
    }

    fun onRegionChange(region: String) {
        _state.update { it.copy(cliente = it.cliente?.copy(region = region, comuna = "")) } // Reset comuna when region changes
    }

    fun onProfileImageChange(path: String) {
        _state.update { it.copy(cliente = it.cliente?.copy(profileImagePath = path)) }
    }

    fun saveChanges() {
        viewModelScope.launch {
            var clienteToUpdate = _state.value.cliente

            if (_state.value.newPassword.isNotBlank()) {
                if (_state.value.newPassword == _state.value.confirmNewPassword) {
                    clienteToUpdate = clienteToUpdate?.copy(contrasena = _state.value.newPassword)
                } else {
                    _state.update { it.copy(message = "Las contraseñas no coinciden") }
                    return@launch
                }
            }

            clienteToUpdate?.let {
                clienteDao.insert(it)
                _state.update { state -> state.copy(message = "Perfil actualizado con éxito", saveSuccess = true) }
            }
        }
    }

    fun messageShown() {
        _state.update { it.copy(message = null, saveSuccess = false) }
    }
}
