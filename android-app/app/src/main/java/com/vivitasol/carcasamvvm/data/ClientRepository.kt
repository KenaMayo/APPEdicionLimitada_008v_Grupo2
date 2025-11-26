package com.vivitasol.carcasamvvm.data

import com.vivitasol.carcasamvvm.model.Cliente
import com.vivitasol.carcasamvvm.remote.ApiClient
import com.vivitasol.carcasamvvm.remote.ClienteService

class ClientRepository {
    private val clienteService = ApiClient.retrofit.create(ClienteService::class.java)

    suspend fun findClientByEmail(email: String): Cliente? {
        val response = clienteService.findByEmail(email)
        return if (response.isSuccessful) response.body() else null
    }
}
