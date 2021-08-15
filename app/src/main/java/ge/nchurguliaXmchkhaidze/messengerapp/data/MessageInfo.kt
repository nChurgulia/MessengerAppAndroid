package ge.nchurguliaXmchkhaidze.messengerapp.data

import java.util.*

data class MessageInfo(var sender: String = "1",
                       var receiver: String = "1",
                       var content: String = "1",
                       var sendTime: String = Date().toString()) {

}
