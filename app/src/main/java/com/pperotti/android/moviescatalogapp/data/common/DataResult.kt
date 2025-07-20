package com.pperotti.android.moviescatalogapp.data.common

/**
 * Generic Result Wrapper that can be reused by more than one repo within the data layer
 *
 * @property T The type of the data offered by this layer.
 */
sealed class DataResult<T> {
    /**
     * The data created as a consequence a successfully executing an
     * operation in the data layer.
     */
    data class Success<T>(val result: T) : DataResult<T>()

    /**
     * The error obtained while executing an operation in the data layer.
     */
    data class Error<T>(val message: String?, val cause: Throwable?) : DataResult<T>()
}
