package ge.nchurguliaXmchkhaidze.messengerapp

data class ChatMessage(val userOne: String,
                       val userTwo: String,
                       val message: String,
                       val time: String,
                       val sender: String)