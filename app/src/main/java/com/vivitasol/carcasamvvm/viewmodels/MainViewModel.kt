package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _startDestination = MutableStateFlow<StartDestination>(StartDestination.Loading)
    val startDestination = _startDestination.asStateFlow()

    init {
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
    }
}
