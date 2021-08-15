package ge.nchurguliaXmchkhaidze.messengerapp


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class LastMessageManagement {
    companion object{
        fun getLastMessageInfo(processLastMessage: (ArrayList<LastMessageInfo>) -> (Boolean) ) {
            val uid = FirebaseAuth.getInstance().uid!!
            val refCurrentUser = FirebaseDatabase.getInstance().getReference("/last-message/$uid")
            var list = ArrayList<LastMessageInfo>()
            refCurrentUser.get().addOnSuccessListener {
                val hmap = it.value as HashMap<Any,Any>?
                if (hmap != null) {
                    for( (key,msgHmap) in hmap ){
                        val msgMap = msgHmap as HashMap<String, String>
                        val lastMsg = LastMessageInfo(key as String, msgMap["content"]!!, msgMap["sendTime"]!!)
                        list.add(lastMsg)
                    }
                    val sortedList = list.sortedWith(compareBy { it.sendTime })
                    list = ArrayList(sortedList.reversed())
                    processLastMessage(list)
                }
            }
        }
        
        fun loadUserData(fromId: String, content: String, date: String, loadOneUser: (ChatInfo) -> (Boolean)) {
            val ref = FirebaseDatabase.getInstance().getReference("/users/$fromId")
            ref.get().addOnSuccessListener {
                val hMap = it.value as HashMap<Any,Any>
                val nickname = hMap["nick"]!! as String
                val job = hMap["job"]!! as String
                val photo = hMap["photo"]!! as String
                val curr = ChatInfo(nickname, fromId, content, date, job, photo)
                loadOneUser(curr)
            }
        }
    }
}