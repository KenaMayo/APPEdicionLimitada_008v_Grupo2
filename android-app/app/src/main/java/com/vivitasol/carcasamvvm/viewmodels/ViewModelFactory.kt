package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivitasol.carcasamvvm.data.ClientRepository
import com.vivitasol.carcasamvvm.data.ProductRepository

class ViewModelFactory(
    private val application: Application,
    private val productRepository: ProductRepository,
    private val clientRepository: ClientRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(clientRepository, application) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(application, productRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(productRepository) as T
            }
            modelClass.isAssignableFrom(CreateProductViewModel::class.java) -> {
                CreateProductViewModel(application, productRepository) as T
            }
            modelClass.isAssignableFrom(UserHomeViewModel::class.java) -> {
                UserHomeViewModel(application, productRepository) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(productRepository) as T
            }
            modelClass.isAssignableFrom(ClienteViewModel::class.java) -> {
                ClienteViewModel() as T
            }
            modelClass.isAssignableFrom(UserProfileViewModel::class.java) -> {
                UserProfileViewModel(application, clientRepository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(application) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
