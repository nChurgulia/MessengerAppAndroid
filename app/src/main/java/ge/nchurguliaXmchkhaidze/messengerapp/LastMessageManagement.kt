package ge.nchurguliaXmchkhaidze.messengerapp

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.reflect.KSuspendFunction1

class LastMessageManagement {
    companion object{

        fun getLastMessageInfo(processLastMessage: (ArrayList<LastMessageInfo>) -> (Boolean) ) {
            var uid = FirebaseAuth.getInstance().uid!!
            var refCurrentUser = FirebaseDatabase.getInstance().getReference("/last-message/$uid")
            var list = ArrayList<LastMessageInfo>()
            refCurrentUser.get().addOnSuccessListener {
                var hmap = it.value as HashMap<Any,Any>?

                if (hmap != null) {
                    //Log.d("CHECKMAP", hmap.toString())
                    for( (key,msgHmap) in hmap ){

                        var msgMap = msgHmap as HashMap<String, String>
                        var lastMsg = LastMessageInfo(key as String, msgMap.get("content")!!, msgMap.get("sendTime")!!)
                        list.add(lastMsg)
                    }


                    var sortedList = list.sortedWith(compareBy({it.sendTime}))

                    list = ArrayList(sortedList.reversed())
                    processLastMessage(list)
                }
            }
        }

        fun loadUserData(fromId: String, content: String, date: String, loadOneUser: (ChatInfo) -> (Boolean)) {

            var ref = FirebaseDatabase.getInstance().getReference("/users/$fromId")
            ref.get().addOnSuccessListener {
                var hMap = it.value as HashMap<Any,Any>
                var nickname = hMap.get("nick")!! as String
                var job = hMap.get("job")!! as String
                var photo = hMap.get("photo")!! as String
                val curr = ChatInfo(nickname, fromId, content, date, job, photo)
                loadOneUser(curr)
            }
        }
    }
}