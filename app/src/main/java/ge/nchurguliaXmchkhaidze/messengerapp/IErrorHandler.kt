package ge.nchurguliaXmchkhaidze.messengerapp

interface IErrorHandler {
    fun handleError(err: String): Boolean
}