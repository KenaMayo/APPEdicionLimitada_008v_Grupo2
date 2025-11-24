package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivitasol.carcasamvvm.data.ProductRepository

class ViewModelFactory(private val application: Application, private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application, repository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CreateProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateProductViewModel(application, repository) as T
        }
        if (modelClass.isAssignableFrom(UserHomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserHomeViewModel(application, repository) as T
        }
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
