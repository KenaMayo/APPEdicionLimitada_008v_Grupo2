package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.ClientRepository
import com.vivitasol.carcasamvvm.data.PrefsRepo
import com.vivitasol.carcasamvvm.model.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserProfileState(
    val cliente: Cliente? = null,
    val profileImageUri: Uri? = null,
    val isLoading: Boolean = true
)

class UserProfileViewModel(
    application: Application,
    private val clientRepository: ClientRepository
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            // Cargar email y datos del cliente
            val email = PrefsRepo.getEmail(getApplication()).first()
            if (email != null) {
                try {
                    val cliente = clientRepository.findClientByEmail(email)
                    _state.update { it.copy(cliente = cliente) }
                } catch (e: Exception) {
                    _state.update { it.copy(isLoading = false) }
                }
            }

            // Cargar URI de la imagen de perfil
            val imageUriString = PrefsRepo.getProfileImageUri(getApplication()).first()
            if (imageUriString != null) {
                _state.update { it.copy(profileImageUri = imageUriString.toUri()) }
            }
            
            _state.update { it.copy(isLoading = false) } // Dejar de cargar al final
        }
    }

    fun onProfileImageChange(uri: Uri) {
        viewModelScope.launch {
            // Actualizar el estado de la UI inmediatamente
            _state.update { it.copy(profileImageUri = uri) }
            // Guardar la URI de forma persistente
            PrefsRepo.setProfileImageUri(getApplication(), uri.toString())
        }
    }
}
