@file:JvmName("FirestoreResponseKt")

package com.usil.aprendeya.data.response

/**
 * T: Generic Type
 * - Esta clase buscar reducir el uso de Try Catch
 * - Tener un manejo acdecuado de errores
 * - Codigo mas limpio y mantenible
 */
sealed class FirestoreResponse<out T> {
    data class Success<out T>(val data: T) : FirestoreResponse<T>()
    data class Error(
        val message: String,
        val throwable: Throwable? = null)
        : FirestoreResponse<Nothing>()

    object Loading: FirestoreResponse<Nothing>()
}

inline fun <T> FirestoreResponse<T>.onSuccess(
    action: (value: T) -> Unit): FirestoreResponse<T> {
    if (this is FirestoreResponse.Success) action (data)
    return this
}

inline fun <T> FirestoreResponse<T>.onError(
    action: (message: String) -> Unit): FirestoreResponse<T> {
    if (this is FirestoreResponse.Error) action(message)
    return this
}

// Manejo de nulls
fun <T> FirestoreResponse<T>.getDataOrNull(): T? {
    return if (this is FirestoreResponse.Success) data else null
}

// Verificaciones de estado
// siempre que usamos operadores de comparación estos retorna un bool
fun <T> FirestoreResponse<T>.isSuccess(): Boolean = this is FirestoreResponse.Success
fun <T> FirestoreResponse<T>.isError(): Boolean = this is FirestoreResponse.Error