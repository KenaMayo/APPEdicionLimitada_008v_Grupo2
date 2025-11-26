package com.vivitasol.carcasamvvm.model

// Modelo para enviar en la petición de creación de un cliente
data class ClienteRequest(
    val nombre: String,
    val email: String,
    val comuna: String,
    val region: String,
    val contrasena: String
)
