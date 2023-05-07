package com.zariya.zariya.core.network

sealed class NetworkResult<out R> {
    data class Success<out T>(val data: T?): NetworkResult<T>()
    data class Error(val exception: String?): NetworkResult<Nothing>()
    object Loading: NetworkResult<Nothing>()
}
