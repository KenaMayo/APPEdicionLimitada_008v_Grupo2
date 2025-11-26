package com.vivitasol.carcasamvvm.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore by preferencesDataStore(name = "demo_prefs")
private val KEY_SUSCRIPCION = booleanPreferencesKey("suscripcion")
private val KEY_EMAIL = stringPreferencesKey("user_email")
private val KEY_PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri") // Clave para la imagen

object PrefsRepo {
    fun suscripcionFlow(context: Context): Flow<Boolean> =
        context.dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { it[KEY_SUSCRIPCION] ?: false }

    suspend fun setSuscripcion(context: Context, value: Boolean) {
        context.dataStore.edit { it[KEY_SUSCRIPCION] = value }
    }

    fun getEmail(context: Context): Flow<String?> =
        context.dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { it[KEY_EMAIL] }

    suspend fun setEmail(context: Context, email: String?) {
        context.dataStore.edit {
            if (email == null) {
                it.remove(KEY_EMAIL)
            } else {
                it[KEY_EMAIL] = email
            }
        }
    }

    // --- Funciones para la URI de la Imagen de Perfil ---
    fun getProfileImageUri(context: Context): Flow<String?> =
        context.dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { it[KEY_PROFILE_IMAGE_URI] }

    suspend fun setProfileImageUri(context: Context, uri: String?) {
        context.dataStore.edit {
            if (uri == null) {
                it.remove(KEY_PROFILE_IMAGE_URI)
            } else {
                it[KEY_PROFILE_IMAGE_URI] = uri
            }
        }
    }
}
