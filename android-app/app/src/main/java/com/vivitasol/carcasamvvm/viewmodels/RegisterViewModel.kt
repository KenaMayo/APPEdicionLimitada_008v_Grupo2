package com.vivitasol.carcasamvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.model.ClienteRequest
import com.vivitasol.carcasamvvm.remote.ApiClient
import com.vivitasol.carcasamvvm.remote.ClienteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val region: String = "",
    val comuna: String = "",
    val password: String = "",
    val confirmPassword: String = "",
)

data class RegisterErrors(
    val name: String? = null,
    val email: String? = null,
    val region: String? = null,
    val comuna: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
)

class RegisterViewModel : ViewModel() {

    private val clienteService = ApiClient.retrofit.create(ClienteService::class.java)

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _errors = MutableStateFlow(RegisterErrors())
    val errors = _errors.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess = _registrationSuccess.asStateFlow()

    val regions = listOf("Metropolitana", "Valparaíso", "Bíobío", "Maule", "Los Lagos", "La Araucanía")
    val comunasByRegion = mapOf(
        "Metropolitana" to listOf("Santiago", "Providencia", "Las Condes", "Ñuñoa", "Maipú", "Puente Alto", "La Florida", "Vitacura"),
        "Valparaíso" to listOf("Valparaíso", "Viña del Mar", "Quilpué", "Villa Alemana", "Quillota"),
        "Bíobío" to listOf("Concepción", "Talcahuano", "Hualpén", "Coronel", "Los Ángeles"),
        "Maule" to listOf("Talca", "Curicó", "Linares", "Cauquenes"),
        "Los Lagos" to listOf("Puerto Montt", "Osorno", "Puerto Varas", "Castro"),
        "La Araucanía" to listOf("Temuco", "Padre Las Casas", "Villarrica", "Angol")
    )

    fun onNameChange(name: String) { _state.update { it.copy(name = name) } }
    fun onEmailChange(email: String) { _state.update { it.copy(email = email) } }
    fun onRegionChange(region: String) { _state.update { it.copy(region = region, comuna = "") } }
    fun onComunaChange(comuna: String) { _state.update { it.copy(comuna = comuna) } }
    fun onPasswordChange(password: String) { _state.update { it.copy(password = password) } }
    fun onConfirmPasswordChange(password: String) { _state.update { it.copy(confirmPassword = password) } }

    fun register() {
        viewModelScope.launch {
            // TODO: Añadir validación de campos
            val currentState = _state.value
            val request = ClienteRequest(
                nombre = currentState.name,
                email = currentState.email,
                comuna = currentState.comuna,
                region = currentState.region,
                contrasena = currentState.password
            )

            try {
                val response = clienteService.createCliente(request)
                if (response.isSuccessful) {
                    _registrationSuccess.value = true
                } else {
                    // Manejar error del servidor (p. ej. email ya existe)
                }
            } catch (e: Exception) {
                // Manejar error de red
            }
        }
    }

    fun registrationShown() {
        _registrationSuccess.value = false
    }
}
