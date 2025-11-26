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
        // *** PASO DE DEPURACIÓN ***
        // Forzamos la ruta de inicio a Login para aislar el problema.
        // Si la app arranca con esto, el problema está en la lógica de decisión de ruta.
        _startDestination.value = StartDestination.Destination(Route.Login.route)

        /*
        viewModelScope.launch {
            val email = PrefsRepo.getEmail(application).first()
            if (email == null) {
                _startDestination.value = StartDestination.Destination(Route.Login.route)
            } else if (email == "admin@edicionlimitada.cl") {
                _startDestination.value = StartDestination.Destination(Route.MenuShell.route)
            } else {
                _startDestination.value = StartDestination.Destination(Route.UserMenuShell.route)
            }
        }
        */
    }
}
