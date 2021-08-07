package ge.nchurguliaXmchkhaidze.messengerapp

import java.util.*

data class MessageInfo(var sender: String,
                       var receiver: String,
                       var content: String,
                       var sendTime: Date)
