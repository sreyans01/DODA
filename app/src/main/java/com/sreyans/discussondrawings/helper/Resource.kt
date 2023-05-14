package com.android.autelsdk.util

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> success(data: T?, msg: String): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

// To make it Compatible with Java
sealed class ResultJ<T> {
    data class Success<T>(val data: T) : ResultJ<T>()
    data class Error<T>(val exception: Exception) : ResultJ<T>()

    companion object {
        @JvmStatic
        fun <T> success(data: T) = Success(data)
        @JvmStatic
        fun <T> error(exception: Exception) = Error<T>(exception)
        @JvmStatic
        fun <T> error(throwable: Throwable) = Error<T>(Exception(throwable))
    }
}