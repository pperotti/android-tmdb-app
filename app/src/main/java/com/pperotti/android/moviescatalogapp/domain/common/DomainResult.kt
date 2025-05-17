package com.pperotti.android.moviescatalogapp.domain.common

/**
 * Generic Result Wrapper that can be reused by more than one repo
 * within the domain layer
 *
 * @property T The type of the data offered by this layer.
 */
sealed class DomainResult<T> {

    /**
     * The data created as a consequence a successfully executing an
     * operation in the domain layer.
     */
    data class Success<T>(val result: T) : DomainResult<T>()

    /**
     * The error obtained while executing an operation in the domain layer.
     */
    data class Error<T>(val message: String?, val cause: Throwable?) : DomainResult<T>()
}
