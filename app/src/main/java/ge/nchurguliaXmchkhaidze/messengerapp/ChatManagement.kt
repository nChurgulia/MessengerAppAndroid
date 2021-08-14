package ge.nchurguliaXmchkhaidze.messengerapp

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap

class ChatManagement {
    companion object{

        fun sendMessage(message: MessageInfo,
                        refSender: DatabaseReference, refReceiver: DatabaseReference ){
            refSender.push().setValue(message).addOnSuccessListener {
                Log.d("Message", "Successfully uploaded to Sender ")
            }
            refReceiver.push().setValue(message).addOnSuccessListener {
                Log.d("Message", "Successfully uploaded to Receiver ")
            }

            val refSenderLastMsg = FirebaseDatabase.getInstance().getReference("/last-message/${message.sender}/${message.receiver}")
            val refReceiverLastMsg = FirebaseDatabase.getInstance().getReference("/last-message/${message.receiver}/${message.sender}")

            val lastMessage = LastMessageInfo(message.sender,message.content,message.sendTime)

            refSenderLastMsg.setValue(lastMessage).addOnSuccessListener {
                Log.d("LastMessage", "Successfuly saved for sender")
            }
            refReceiverLastMsg.setValue(lastMessage).addOnSuccessListener {
                Log.d("LastMessage", "Successfuly saved for receiver")
            }
        }

        fun getConversation(userOne: String, userTwo: String, senderRef: DatabaseReference, processConv: (ArrayList<MessageInfo>) -> Boolean) {
            //val uid = FirebaseAuth.getInstance().uid ?: ""
            //val ref = FirebaseDatabase.getInstance().getReference("/message/$userOne/$userTwo")

            senderRef.get()
                    .addOnSuccessListener {
                        var list = ArrayList<MessageInfo>()
                        var hmap: HashMap<Any,Any>

                        if(it.value != null){
                            hmap = it.value as HashMap<Any,Any>
//                            hmap = it.value as HashMap<String, MessageInfo>
                            Log.d("ReadConv", "Start to read")
                            for( (key,msg) in hmap){
                                Log.d("ReadConv", msg.toString())
                                var msgMap = msg as HashMap<String, Any>
                                if(msgMap != null){
                                    var sender = msgMap.get("sender") as String?
                                    var receiver = msgMap.get("receiver") as String?
                                    var content = msgMap.get("content") as String?
                                    var sendTime =  msgMap.get("sendTime") as String?
                                    if(sender != null && receiver != null && content != null && sendTime != null){
                                        var msg = MessageInfo(sender, receiver, content, sendTime)
                                        Log.d("ReadConv", msg.content)
                                        list.add(msg)
                                    }

                                }

                            }
                            Log.d("ReadConv", "already read")
                            processConv(list)
                        }else{
                            Log.d("ReadConv", "Cannot get data")
                        }
                        //Log.d("ReadConv", "info read: $value")
                    }
                    .addOnFailureListener{
                        Log.d("ReadInfo", "Failed to read info")
                    }
        }


    }
}