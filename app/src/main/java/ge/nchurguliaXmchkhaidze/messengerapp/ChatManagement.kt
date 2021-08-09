package ge.nchurguliaXmchkhaidze.messengerapp

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
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
        }

        fun getConversation(userOne: String, userTwo: String, senderRef: DatabaseReference, processConv: (ArrayList<MessageInfo>) -> Boolean) {
            //val uid = FirebaseAuth.getInstance().uid ?: ""
            //val ref = FirebaseDatabase.getInstance().getReference("/message/$userOne/$userTwo")

            senderRef.get()
                    .addOnSuccessListener {
                        var list = ArrayList<MessageInfo>()
                        var hmap: HashMap<Any,Any>
                        var dataSnapshot: DataSnapshot
                        if(it.value != null){
                            hmap = it.value as HashMap<Any,Any>
//                            hmap = it.value as HashMap<String, MessageInfo>
                            Log.d("ReadConv", "Start to read")
                            for( (key,msg) in hmap){
                                Log.d("ReadConv", msg.toString())
                                var msgMap = msg as HashMap<String, Any>
                                var msg = MessageInfo(msgMap.get("sender") as String,msgMap.get("receiver") as String,msgMap.get("content") as String, msgMap.get("sendTime") as String)
                                Log.d("ReadConv", msg.content)
                                list.add(msg)
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