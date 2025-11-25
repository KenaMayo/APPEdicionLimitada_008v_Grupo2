package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.AppDatabase
import com.vivitasol.carcasamvvm.model.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val comuna: String = "",
    val region: String = ""
)

data class RegisterErrors(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val comuna: String? = null,
    val region: String? = null
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _errors = MutableStateFlow(RegisterErrors())
    val errors: StateFlow<RegisterErrors> = _errors.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    val regions = listOf("Metropolitana", "Valparaíso", "Bíobío", "Maule", "Los Lagos", "La Araucanía")
    val comunasByRegion = mapOf(
        "Metropolitana" to listOf("Santiago", "Providencia", "Las Condes", "Ñuñoa", "Maipú", "Puente Alto", "La Florida", "Vitacura"),
        "Valparaíso" to listOf("Valparaíso", "Viña del Mar", "Quilpué", "Villa Alemana", "Quillota"),
        "Bíobío" to listOf("Concepción", "Talcahuano", "Hualpén", "Coronel", "Los Ángeles"),
        "Maule" to listOf("Talca", "Curicó", "Linares", "Cauquenes"),
        "Los Lagos" to listOf("Puerto Montt", "Osorno", "Puerto Varas", "Castro"),
        "La Araucanía" to listOf("Temuco", "Padre Las Casas", "Villarrica", "Angol")
    )

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
        _errors.update { it.copy(name = null) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
        _errors.update { it.copy(email = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
        _errors.update { it.copy(password = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword) }
        _errors.update { it.copy(confirmPassword = null) }
    }

    fun onComunaChange(comuna: String) {
        _state.update { it.copy(comuna = comuna) }
        _errors.update { it.copy(comuna = null) }
    }

    fun onRegionChange(region: String) {
        _state.update { it.copy(region = region, comuna = "") } // Reset comuna when region changes
        _errors.update { it.copy(region = null, comuna = null) }
    }

    private fun validate(): Boolean {
        val currentState = _state.value
        val nameError = if (currentState.name.isBlank()) "El nombre es obligatorio" else null
        val emailError = if (currentState.email.isBlank()) {
            "El correo es obligatorio"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            "El correo no es válido"
        } else {
            null
        }
        val passwordError = if (currentState.password.isBlank()) "La contraseña es obligatoria" else null
        val confirmPasswordError = if (currentState.confirmPassword.isBlank()) {
            "Vuelva a escribir la contraseña"
        } else if (currentState.password != currentState.confirmPassword) {
            "Las contraseñas no coinciden"
        } else {
            null
        }
        val comunaError = if (currentState.comuna.isBlank()) "La comuna es obligatoria" else null
        val regionError = if (currentState.region.isBlank()) "La región es obligatoria" else null

        _errors.update {
            it.copy(
                name = nameError,
                email = emailError,
                password = passwordError,
                confirmPassword = confirmPasswordError,
                comuna = comunaError,
                region = regionError
            )
        }

        return nameError == null && emailError == null && passwordError == null && confirmPasswordError == null &&
            comunaError == null && regionError == null
    }

    fun register() {
        if (validate()) {
            viewModelScope.launch {
                val cliente = Cliente(
                    nombre = state.value.name,
                    email = state.value.email,
                    contrasena = state.value.password,
                    comuna = state.value.comuna,
                    region = state.value.region
                )
                clienteDao.insert(cliente)
                _registrationSuccess.value = true
            }
        }
    }

    fun registrationShown() {
        _registrationSuccess.value = false
    }
}
