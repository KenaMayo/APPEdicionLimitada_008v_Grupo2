package com.vivitasol.carcasamvvm.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "appedicionlimitada008vgrupo2-production.up.railway.app"

    // Interceptor para limpiar headers de autorización inválidos
    private val authCleanupInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val authHeader = originalRequest.header("Authorization")

        if (authHeader.isNullOrBlank() || authHeader == "Bearer null") {
            // Si el header es nulo, vacío o "Bearer null", lo eliminamos
            val newRequest = originalRequest.newBuilder()
                .removeHeader("Authorization")
                .build()
            chain.proceed(newRequest)
        } else {
            // Si hay un header válido, dejamos que continúe
            chain.proceed(originalRequest)
        }
    }

    val retrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authCleanupInterceptor) //Añadimos el nuevo interceptor
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(NullOnEmptyConverterFactory()) // Añadido para manejar respuestas vacías
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
