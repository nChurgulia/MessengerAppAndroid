package ge.nchurguliaXmchkhaidze.messengerapp.data

data class LastMessageInfo (var fromId: String,
                            var content: String,
                            var sendTime: String){

    companion object {
        const val LAST_MESSAGE = "last-message"
        const val SENDER = "sender"
        const val RECEIVER = "receiver"
        const val CONTENT = "content"
        const val SEND_TIME = "sendTime"
    }
}
