package com.vivitasol.carcasamvvm.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginState(
    val email: String = "",
    val pass: String = ""
)

data class LoginErrors(
    val email: String? = null,
    val pass: String? = null
)

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _errors = MutableStateFlow(LoginErrors())
    val errors = _errors.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
        _errors.update { it.copy(email = null) }
    }

    fun onPassChange(pass: String) {
        _state.update { it.copy(pass = pass) }
        _errors.update { it.copy(pass = null) }
    }

    fun validate(): Boolean {
        val currentState = _state.value
        val emailError = if (currentState.email.isBlank()) {
            "El correo es obligatorio"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            "El correo no es válido"
        } else {
            null
        }

        val passError = if (currentState.pass.isBlank()) {
            "La contraseña es obligatoria"
        } else {
            null
        }

        _errors.update { it.copy(email = emailError, pass = passError) }

        return emailError == null && passError == null
    }
}
