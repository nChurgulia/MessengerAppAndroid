package ge.nchurguliaXmchkhaidze.messengerapp.model

import com.google.firebase.database.*
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo.Companion.JOB
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo.Companion.NICK
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo.Companion.PHOTO
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo.Companion.USERS

class UserSearchManagement {

    private val limit = 5
    var lastNode: String? = ""
    var keyword: String? = ""

    fun lazyLoadUsers(updateData: (uid: String?, nickname: String?, job: String?, photo: String?) -> Boolean, handleError: (String) -> Boolean) {
        val query: Query

        if (keyword == "") {
            query = if (lastNode == "") {
                FirebaseDatabase.getInstance().reference
                        .child(USERS)
                        .orderByChild(NICK)
                        .limitToFirst(limit)
            } else {
                FirebaseDatabase.getInstance().reference
                        .child(USERS)
                        .orderByChild(NICK)
                        .startAfter(lastNode)
                        .limitToFirst(limit)
            }
        } else {
            query = FirebaseDatabase.getInstance().reference
                    .child(USERS)
                    .orderByChild(NICK)
                    .startAt(keyword)
                    .endAt(keyword + "\uf8ff")
        }

        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.children.count() == 0){
                    updateData(null, null, null, null)
                }

                for (i in snapshot.children) {
                    val uid = i.key
                    val hMap = i.value as HashMap<String, String>
                    val nickname = hMap[NICK]
                    val job = hMap[JOB]
                    val photo = hMap[PHOTO]
                    if (keyword == "") lastNode = nickname
                    updateData(uid, nickname, job, photo)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                handleError(error.message)
            }
        })
    }
}