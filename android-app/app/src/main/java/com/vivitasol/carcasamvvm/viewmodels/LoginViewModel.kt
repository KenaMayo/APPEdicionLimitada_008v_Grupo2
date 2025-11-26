package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.PrefsRepo
import com.vivitasol.carcasamvvm.remote.ApiClient
import com.vivitasol.carcasamvvm.remote.ClienteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class LoginResult {
    object Neutral : LoginResult()
    object AdminSuccess : LoginResult()
    object UserSuccess : LoginResult()
    data class Error(val message: String) : LoginResult()
}

data class LoginState(
    val email: String = "",
    val pass: String = ""
)

data class LoginErrors(
    val email: String? = null,
    val pass: String? = null,
    val general: String? = null
)

class LoginViewModel(private val application: Application) : AndroidViewModel(application) {

    private val clienteService = ApiClient.retrofit.create(ClienteService::class.java)

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _errors = MutableStateFlow(LoginErrors())
    val errors = _errors.asStateFlow()

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Neutral)
    val loginResult = _loginResult.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPassChange(pass: String) {
        _state.update { it.copy(pass = pass) }
    }

    fun login() {
        viewModelScope.launch {
            // Lógica de validación básica
            if (_state.value.email.isBlank() || _state.value.pass.isBlank()) {
                _errors.value = LoginErrors(general = "Email y contraseña no pueden estar vacíos")
                return@launch
            }

            try {
                // ¡CORREGIDO! El nombre del campo debe ser "contrasena" para que coincida con el backend.
                val loginInfo = mapOf("email" to _state.value.email, "contrasena" to _state.value.pass)
                val response = clienteService.login(loginInfo)

                if (response.isSuccessful) {
                    val cliente = response.body()
                    PrefsRepo.setEmail(application, cliente?.email)

                    if (_state.value.email == "admin@edicionlimitada.cl") {
                        _loginResult.value = LoginResult.AdminSuccess
                    } else {
                        _loginResult.value = LoginResult.UserSuccess
                    }
                } else {
                    _loginResult.value = LoginResult.Error("Credenciales incorrectas")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Error de red: ${e.message}")
            }
        }
    }

    fun resetLoginResult() {
        _loginResult.value = LoginResult.Neutral
    }
}
