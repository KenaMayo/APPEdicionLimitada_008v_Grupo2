package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.ProductRepository
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateProductState(
    val name: String = "",
    val designer: String = "",
    val price: String = "", // Usamos String para los campos de texto
    val stock: String = "", // Usamos String para los campos de texto
    val imageUri: Uri? = null,
    val saveSuccess: Boolean = false,
    val message: String? = null
)

class CreateProductViewModel(application: Application, private val repository: ProductRepository) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(CreateProductState())
    val state = _state.asStateFlow()

    private val _latestTmpUri = MutableStateFlow<Uri?>(null)
    val latestTmpUri: StateFlow<Uri?> = _latestTmpUri

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onDesignerChange(designer: String) {
        _state.update { it.copy(designer = designer) }
    }

    fun onPriceChange(price: String) {
        _state.update { it.copy(price = price) }
    }

    fun onStockChange(stock: String) {
        _state.update { it.copy(stock = stock) }
    }

    fun onImageSelected(uri: Uri?) {
        _state.update { it.copy(imageUri = uri) }
    }

    fun onTmpUriReady(uri: Uri) {
        _latestTmpUri.update { uri }
    }

    fun saveProduct() {
        viewModelScope.launch {
            try {
                val currentState = _state.value
                if (currentState.imageUri == null || currentState.name.isBlank()) {
                    _state.update { it.copy(message = "Nombre e imagen son obligatorios") }
                    return@launch
                }
                val product = Product(
                    name = currentState.name,
                    designer = currentState.designer,
                    price = currentState.price.toDoubleOrNull() ?: 0.0,
                    stock = currentState.stock.toIntOrNull() ?: 0,
                    imageUri = currentState.imageUri.toString()
                )
                repository.insert(product)
                _state.update { it.copy(saveSuccess = true, message = "Producto guardado con éxito") }
            } catch (e: Exception) {
                _state.update { it.copy(saveSuccess = false, message = "Error al guardar: ${e.message}") }
            }
        }
    }

    fun onSaveHandled() {
        _state.update { CreateProductState() } // Reset state after saving
    }
}
