package com.vivitasol.carcasamvvm.remote

import com.vivitasol.carcasamvvm.model.Cliente
import com.vivitasol.carcasamvvm.model.ClienteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClienteService {

    // --- AUTH ENDPOINTS --- //
    @POST("auth/register")
    suspend fun register(@Body cliente: ClienteRequest): Response<Cliente>

    @POST("auth/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<Cliente>

    // --- CLIENTES ENDPOINTS --- //
    @GET("clientes")
    suspend fun getClientes(): List<Cliente>

    @GET("clientes/search")
    suspend fun findByEmail(@Query("email") email: String): Response<Cliente>

    @PUT("clientes/{id}")
    suspend fun updateCliente(@Path("id") id: String, @Body cliente: Cliente): Response<Cliente>
}
