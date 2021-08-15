package ge.nchurguliaXmchkhaidze.messengerapp.presenter

interface IErrorHandler {
    fun handleError(err: String): Boolean
}