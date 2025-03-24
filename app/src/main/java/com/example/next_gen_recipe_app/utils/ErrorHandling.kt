package com.example.next_gen_recipe_app.utils

/**
 * A sealed class to wrap API results, encapsulating success and error states.
 */
sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Exception) : ResultWrapper<Nothing>()
}

/**
 * Extension function to convert a standard Result to our ResultWrapper type.
 */
fun <T> Result<T>.toResultWrapper(): ResultWrapper<T> {
    return fold(
        onSuccess = { ResultWrapper.Success(it) },
        onFailure = { ResultWrapper.Error(it as Exception) }
    )
}
