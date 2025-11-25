package com.vivitasol.carcasamvvm.viewmodels

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vivitasol.carcasamvvm.data.ProductRepository
import com.vivitasol.carcasamvvm.model.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application, private val repository: ProductRepository) : AndroidViewModel(application) {

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun resourceToUri(resourceId: Int): Uri {
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(getApplication<Application>().resources.getResourcePackageName(resourceId))
            .appendPath(getApplication<Application>().resources.getResourceTypeName(resourceId))
            .appendPath(getApplication<Application>().resources.getResourceEntryName(resourceId))
            .build()
    }
}