package ge.nchurguliaXmchkhaidze.messengerapp

import com.google.firebase.database.*

class UserSearchManagement {

    private val limit = 5
    var lastNode: String? = ""
    var keyword: String? = ""

    fun lazyLoadUsers(updateData: (uid: String?, nickname: String?, job: String?, photo: String?) -> Boolean, handleError: (String) -> Boolean) {
        val query: Query

        if (keyword == "") {
            query = if (lastNode == "") {
                FirebaseDatabase.getInstance().reference
                        .child(ManageInfo.USERS)
                        .orderByChild(ManageInfo.NICKNAME)
                        .limitToFirst(limit)
            } else {
                FirebaseDatabase.getInstance().reference
                        .child(ManageInfo.USERS)
                        .orderByChild(ManageInfo.NICKNAME)
                        .startAfter(lastNode)
                        .limitToFirst(limit)
            }
        } else {
            query = FirebaseDatabase.getInstance().reference
                    .child(ManageInfo.USERS)
                    .orderByChild(ManageInfo.NICKNAME)
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
                    val nickname = hMap[ManageInfo.NICKNAME]
                    val job = hMap[ManageInfo.JOB]
                    val photo = hMap[ManageInfo.PHOTO]
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