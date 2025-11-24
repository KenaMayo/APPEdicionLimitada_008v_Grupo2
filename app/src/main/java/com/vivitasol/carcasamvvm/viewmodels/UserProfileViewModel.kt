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

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val clienteDao = AppDatabase.getDatabase(application).clienteDao()

    private val _cliente = MutableStateFlow<Cliente?>(null)
    val cliente = _cliente.asStateFlow()

    fun loadCliente() {
        viewModelScope.launch {
            val email = PrefsRepo.getEmail(getApplication()).first()
            if (email != null) {
                _cliente.value = clienteDao.findByEmail(email)
            }
        }
    }
}
