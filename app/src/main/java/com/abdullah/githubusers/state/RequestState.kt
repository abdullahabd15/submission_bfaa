package com.abdullah.githubusers.state

sealed class RequestState<T> {
    class Progress<T> : RequestState<T>()
    data class RequestSucceed<T>(val data: T?): RequestState<T>()
    data class RequestFailed<T>(val message: String): RequestState<T>()
}
