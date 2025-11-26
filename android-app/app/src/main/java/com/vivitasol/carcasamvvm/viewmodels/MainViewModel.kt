package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.ClientRepository
import com.vivitasol.carcasamvvm.data.PrefsRepo
import com.vivitasol.carcasamvvm.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class StartDestination {
    object Loading : StartDestination()
    data class Destination(val route: String) : StartDestination()
}

class MainViewModel(private val clientRepository: ClientRepository, application: Application) : ViewModel() {
    private val _startDestination = MutableStateFlow<StartDestination>(StartDestination.Loading)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val email = PrefsRepo.getEmail(application).first()
            if (email == null) {
                _startDestination.value = StartDestination.Destination(Route.Login.route)
            } else if (email == "admin@edicionlimitada.cl") {
                // Si el email es el del admin, vamos directo al menú de admin
                _startDestination.value = StartDestination.Destination(Route.MenuShell.route)
            } else {
                // Para cualquier otro email, vamos al menú de usuario
                _startDestination.value = StartDestination.Destination(Route.UserMenuShell.route)
            }
        }
    }
}

// Esta MainViewModelFactory ya no es necesaria, usaremos la ViewModelFactory unificada
// La dejo aquí comentada por si la necesitamos, pero la eliminaremos en el siguiente paso.
/*
class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val clientRepository = (application as com.vivitasol.carcasamvvm.LimitedEditionApp).clientRepository
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(clientRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
*/
