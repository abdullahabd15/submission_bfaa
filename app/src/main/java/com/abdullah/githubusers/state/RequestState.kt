package com.abdullah.githubusers.state

sealed class RequestState<T> {
    class Init<T> : RequestState<T>()
    class Progress<T> : RequestState<T>()
    data class RequestSucceed<T>(val data: T?) : RequestState<T>()
    data class RequestFailed<T>(var message: String? = null) : RequestState<T>()
    class RequestFinished<T>() : RequestState<T>()
}