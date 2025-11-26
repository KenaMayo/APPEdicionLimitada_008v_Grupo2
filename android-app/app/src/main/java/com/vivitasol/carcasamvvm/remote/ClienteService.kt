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

    @GET("clientes")
    suspend fun getClientes(): List<Cliente>

    @POST("clientes")
    suspend fun createCliente(@Body cliente: ClienteRequest): Response<Unit> // Usamos Response<Unit> para saber si fue exitoso

    @GET("clientes/search")
    suspend fun findByEmail(@Query("email") email: String): Response<Cliente> // Asumimos un endpoint de búsqueda

    @POST("clientes/login")
    suspend fun login(@Body loginInfo: Map<String, String>): Response<Cliente> // Body simple para email/pass

    @PUT("clientes/{id}")
    suspend fun updateCliente(@Path("id") id: String, @Body cliente: Cliente): Response<Cliente>
}
