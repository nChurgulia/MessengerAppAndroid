package ge.nchurguliaXmchkhaidze.messengerapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LastMessageManagement {
    companion object{

        fun getLastMessageInfo(processLastMessage: (ArrayList<LastMessageInfo>) -> (Boolean) ) {
            val uid = FirebaseAuth.getInstance().uid!!
            val refCurrentUser = FirebaseDatabase.getInstance().getReference("/last-message/$uid")
            var list = ArrayList<LastMessageInfo>()
            refCurrentUser.get().addOnSuccessListener { it ->
                val hmap = it.value as HashMap<Any,Any>?

                if (hmap != null) {
                    //Log.d("CHECKMAP", hmap.toString())
                    for( (key,msgHmap) in hmap ){

                        val msgMap = msgHmap as HashMap<String, String>
                        val lastMsg = LastMessageInfo(key as String, msgMap["content"]!!, msgMap["sendTime"]!!)
                        list.add(lastMsg)
                    }


                    var sortedList = list.sortedWith(compareBy { Date(it.sendTime) })

                    list = ArrayList(sortedList.reversed())
                    processLastMessage(list)
                }else{
                    processLastMessage(ArrayList())
                }
            }
        }

        fun loadUserData(fromId: String, content: String, date: String, loadOneUser: (ChatInfo) -> (Boolean)) {

            val ref = FirebaseDatabase.getInstance().getReference("/users/$fromId")
            ref.get().addOnSuccessListener {
                val hMap = it.value as HashMap<Any, Any>
                val nickname = hMap["nick"]!! as String
                val job = hMap["job"]!! as String
                val photo = hMap["photo"]!! as String
                val curr = ChatInfo(nickname, fromId, content, date, job, photo)
                loadOneUser(curr)
            }
        }
    }
}