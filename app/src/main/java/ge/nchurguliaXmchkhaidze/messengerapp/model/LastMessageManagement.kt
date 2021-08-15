package ge.nchurguliaXmchkhaidze.messengerapp.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ge.nchurguliaXmchkhaidze.messengerapp.data.ChatInfo
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo.Companion.CONTENT
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo.Companion.LAST_MESSAGE
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo.Companion.SEND_TIME
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo.Companion.JOB
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo.Companion.NICK
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo.Companion.PHOTO
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo.Companion.USERS
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LastMessageManagement {
    companion object{

        fun getLastMessageInfo(processLastMessage: (ArrayList<LastMessageInfo>) -> (Boolean), handleError: (String) -> (Boolean)) {
            val uid = FirebaseAuth.getInstance().uid!!
            val refCurrentUser = FirebaseDatabase.getInstance().getReference("/$LAST_MESSAGE/$uid")
            var list = ArrayList<LastMessageInfo>()
            refCurrentUser.get().addOnSuccessListener { it ->
                val hmap = it.value as HashMap<Any,Any>?
                if (hmap != null) {
                    for( (key,msgHmap) in hmap ){
                        val msgMap = msgHmap as HashMap<String, String>
                        val lastMsg = LastMessageInfo(key as String, msgMap[CONTENT]!!, msgMap[SEND_TIME]!!)
                        list.add(lastMsg)
                    }
                    val sortedList = list.sortedWith(compareBy { Date(it.sendTime) })
                    list = ArrayList(sortedList.reversed())
                    processLastMessage(list)
                }else{
                    processLastMessage(ArrayList())
                }
            }.addOnFailureListener {
                it.message?.let { it1 -> handleError(it1) }
            }
        }

        fun loadUserData(fromId: String, content: String, date: String, loadOneUser: (ChatInfo) -> (Boolean), handleError: (String) -> (Boolean)) {
            val ref = FirebaseDatabase.getInstance().getReference("/$USERS/$fromId")
            ref.get().addOnSuccessListener {
                val hMap = it.value as HashMap<Any, Any>
                val nickname = hMap[NICK]!! as String
                val job = hMap[JOB]!! as String
                val photo = hMap[PHOTO]!! as String
                val curr = ChatInfo(nickname, fromId, content, date, job, photo)
                loadOneUser(curr)
            }.addOnFailureListener {
                it.message?.let { it1 -> handleError(it1) }
            }
        }
    }
}