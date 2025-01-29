package com.prafullkumar.commons

sealed interface BaseClass<out T> {
    data class Success<out T>(val data: T) : BaseClass<T>
    data class Error(val exception: Exception) : BaseClass<Nothing>
    data object Loading : BaseClass<Nothing>
}