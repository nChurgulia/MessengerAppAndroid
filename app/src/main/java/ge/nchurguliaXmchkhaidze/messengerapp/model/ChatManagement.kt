package ge.nchurguliaXmchkhaidze.messengerapp.model

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo.Companion.CONTENT
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo.Companion.LAST_MESSAGE
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo.Companion.RECEIVER
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo.Companion.SENDER
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo.Companion.SEND_TIME
import ge.nchurguliaXmchkhaidze.messengerapp.data.MessageInfo
import java.util.*
import kotlin.collections.HashMap

class ChatManagement {
    companion object{

        fun sendMessage(message: MessageInfo, refSender: DatabaseReference, refReceiver: DatabaseReference, handleError: (String) -> Boolean){

            refSender.push().setValue(message).addOnSuccessListener {
                Log.d("Message", "Successfully uploaded to Sender ")
            }.addOnFailureListener {
                it.message?.let { it1 -> handleError(it1) }
            }

            refReceiver.push().setValue(message).addOnSuccessListener {
                Log.d("Message", "Successfully uploaded to Receiver ")
            }.addOnFailureListener {
                it.message?.let { it1 -> handleError(it1) }
            }

            val refSenderLastMsg = FirebaseDatabase.getInstance().getReference("/${LAST_MESSAGE}/${message.sender}/${message.receiver}")
            val refReceiverLastMsg = FirebaseDatabase.getInstance().getReference("/${LAST_MESSAGE}/${message.receiver}/${message.sender}")

            val lastMessage = LastMessageInfo(message.sender,message.content,message.sendTime)

            refSenderLastMsg.setValue(lastMessage).addOnSuccessListener {
                Log.d("LastMessage", "Successfuly saved for sender")
            }.addOnFailureListener {
                it.message?.let { it1 -> handleError(it1) }
            }

            refReceiverLastMsg.setValue(lastMessage).addOnSuccessListener {
                Log.d("LastMessage", "Successfuly saved for receiver")
            }.addOnFailureListener {
                it.message?.let { it1 -> handleError(it1) }
            }
        }

        fun getConversation(senderRef: DatabaseReference, processConv: (ArrayList<MessageInfo>) -> Boolean, handleError: (String) -> Boolean) {
            senderRef.get()
                    .addOnSuccessListener {
                        val list = ArrayList<MessageInfo>()
                        val hmap: HashMap<Any,Any>

                        if(it.value != null){
                            hmap = it.value as HashMap<Any,Any>
                            Log.d("ReadConv", "Start to read")
                            for((_,msg) in hmap){
                                Log.d("ReadConv", msg.toString())
                                val msgMap = msg as HashMap<String, Any>
                                val sender = msgMap[SENDER] as String?
                                val receiver = msgMap[RECEIVER] as String?
                                val content = msgMap[CONTENT] as String?
                                val sendTime =  msgMap[SEND_TIME] as String?
                                if(sender != null && receiver != null && content != null && sendTime != null){
                                    val message = MessageInfo(sender, receiver, content, sendTime)
                                    Log.d("ReadConv", message.content)
                                    list.add(message)
                                }
                            }
                            Log.d("ReadConv", "already read")
                            processConv(list)
                        }else{
                            processConv(list)
                            Log.d("ReadConv", "Cannot get data")
                        }
                    }
                    .addOnFailureListener{
                        it.message?.let { it1 -> handleError(it1) }
                        Log.d("ReadInfo", "Failed to read info")
                    }
        }
    }
}