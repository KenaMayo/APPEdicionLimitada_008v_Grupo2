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
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val clienteService = ApiClient.retrofit.create(ClienteService::class.java)

    private val _cliente = MutableStateFlow<Cliente?>(null)
    val cliente = _cliente.asStateFlow()

    init {
        viewModelScope.launch {
            val email = PrefsRepo.getEmail(application).first()
            if (email != null) {
                try {
                    val response = clienteService.findByEmail(email)
                    if (response.isSuccessful) {
                        _cliente.value = response.body()
                    }
                } catch (e: Exception) {
                    // Manejar error de red
                }
            }
        }
    }
}
