package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.AppDatabase
import com.vivitasol.carcasamvvm.data.PrefsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val pass: String = ""
)

data class LoginErrors(
    val email: String? = null,
    val pass: String? = null,
    val general: String? = null
)

sealed class LoginResult {
    object Idle : LoginResult()
    object AdminSuccess : LoginResult()
    object UserSuccess : LoginResult()
    object Error : LoginResult()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val prefsRepo = PrefsRepo

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _errors = MutableStateFlow(LoginErrors())
    val errors = _errors.asStateFlow()

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult = _loginResult.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
        _errors.update { it.copy(email = null, general = null) }
    }

    fun onPassChange(pass: String) {
        _state.update { it.copy(pass = pass) }
        _errors.update { it.copy(pass = null, general = null) }
    }

    fun login() {
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

        if (emailError != null || passError != null) {
            _errors.update { it.copy(email = emailError, pass = passError) }
            return
        }

        viewModelScope.launch {
            val user = userDao.findByEmail(currentState.email)
            if (user == null || user.contrasena != currentState.pass) {
                _errors.update { it.copy(general = "Correo o contraseña incorrectos") }
                _loginResult.value = LoginResult.Error
            } else {
                prefsRepo.setEmail(getApplication(), user.email)
                if (user.email == "admin@edicionlimitada.cl") {
                    _loginResult.value = LoginResult.AdminSuccess
                } else {
                    _loginResult.value = LoginResult.UserSuccess
                }
            }
        }
    }

    fun resetLoginResult() {
        _loginResult.value = LoginResult.Idle
    }
}
