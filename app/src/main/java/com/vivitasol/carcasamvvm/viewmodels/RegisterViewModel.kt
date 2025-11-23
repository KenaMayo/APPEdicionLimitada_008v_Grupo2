package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.AppDatabase
import com.vivitasol.carcasamvvm.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

data class RegisterErrors(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _errors = MutableStateFlow(RegisterErrors())
    val errors: StateFlow<RegisterErrors> = _errors.asStateFlow()
    
    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

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

        _errors.update {
            it.copy(
                name = nameError,
                email = emailError,
                password = passwordError,
                confirmPassword = confirmPasswordError
            )
        }

        return nameError == null && emailError == null && passwordError == null && confirmPasswordError == null
    }

    fun register() {
        if (validate()) {
            viewModelScope.launch {
                val user = User(
                    nombre = state.value.name,
                    email = state.value.email,
                    contrasena = state.value.password
                )
                userDao.insert(user)
                _registrationSuccess.value = true
            }
        }
    }
    
    fun registrationShown() {
        _registrationSuccess.value = false
    }
}
