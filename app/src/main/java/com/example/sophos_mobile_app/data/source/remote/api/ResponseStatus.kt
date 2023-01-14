package com.example.sophos_mobile_app.data.source.remote.api

sealed class ResponseStatus<T>{
    class Success<T>(val data: T): ResponseStatus<T>()
    class Loading<T>: ResponseStatus<T>()
    class Error<T>(val messageId: Int): ResponseStatus<T>()
}
