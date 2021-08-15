package ge.nchurguliaXmchkhaidze.messengerapp

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap


class ManageInfo {
    companion object {
        const val USERS = "users"
        const val NICKNAME = "nick"
        const val JOB = "job"
        const val PHOTO = "photo"
        const val UID = "uid"

        fun uploadUserInformation(nick: String, job: String, imageUri: Uri?, imageURL: String, actionAfterLogged: (() -> Boolean)?){
            if(imageUri == null) {
                uploadInfo(nick, job, imageURL, actionAfterLogged)
                return
            }
            Log.d("NULLCHECK", imageUri.toString())
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(imageUri)
                    .addOnSuccessListener {
                        Log.d("Storage", "Succesfully uploaded image ${it.metadata?.path}")
                        ref.downloadUrl.addOnSuccessListener {
                            Log.d("Storage", "Photo URL is: $it")
                            uploadInfo(nick, job, it.toString(), actionAfterLogged)
                        }
                    }
        }



        fun getInfo(processInfo: (String, String, String) -> Boolean){
            val uid = FirebaseAuth.getInstance().uid ?: ""
            var ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            ref.get().addOnSuccessListener {
                var hMap = it.value as HashMap<Any,Any>
                var nickname = hMap.get("nick")!! as String
                var job = hMap.get("job")!! as String
                var photo = hMap.get("photo")!! as String
                processInfo(nickname, job, photo)
            }
        }

         private fun uploadInfo(nick: String, job: String, photoUrl: String, actionAfterLogged: (() -> Boolean)?){
             val uid = FirebaseAuth.getInstance().uid ?: ""
             val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
             var userDetails = HashMap<String, String> ()
             userDetails["nick"] = nick
             userDetails["job"] = job
             userDetails["photo"] = photoUrl
             ref.setValue(userDetails)
                .addOnSuccessListener {
                    Log.d("UserInfo", "info added: $job")
                    if (actionAfterLogged != null) {
                        actionAfterLogged()
                    }
                }
                .addOnFailureListener{
                    Log.d("UserInfo", "Failed to add info")
//              showWarning(R.id.job_pr, it.toString())
                }
         }



        fun filterUsers(keyword: String, updateData: (uid: String?, nickname: String?, job: String?, photo: String?) -> Boolean) {
            FirebaseDatabase.getInstance().reference
                .child(USERS)
                .orderByChild(NICKNAME)
                .startAt(keyword)
                .endAt(keyword + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val messages = snapshot.getValue(object: GenericTypeIndicator<HashMap<String, HashMap<String, String>>>(){})
                        if (messages != null) {
                            for (i in messages) {
                                val uid = i.key
                                val nickname = i.value[NICKNAME]
                                val job = i.value[JOB]
                                val photo = i.value[PHOTO]
                                updateData(uid, nickname, job, photo)
                            }
                        }else{
                            updateData(null, null, null, null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })



        }

        //-------------------------------------------
//        fun getUserInfo(processInfo: (UserInfo) -> Boolean){
//            val uid = FirebaseAuth.getInstance().uid ?: ""
//            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//
//            ref.get()
//                .addOnSuccessListener {
//                    val user = it.getValue(UserInfo::class.java)
//                    Log.d("ReadInfo", "info read: ${user.nickname}")
//                    processInfo(user)
//                }
//                .addOnFailureListener{
//                    Log.d("ReadInfo", "Failed to read info")
//                }
//        }
//
//        fun uploadInfo(nickname: String, job: String, imageUri: Uri?){
//            if(imageUri == null) return
//            val filename = UUID.randomUUID().toString()
//            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
//            ref.putFile(imageUri!!)
//                .addOnSuccessListener {
//                    Log.d("Storage","Succesfully uploaded image ${it.metadata?.path}")
//                    ref.downloadUrl.addOnSuccessListener {
//                        Log.d("Storage", "Photo URL is: $it")
//                        var user = UserInfo()
//                        uploadUserInfo(nickname,job,it.toString())
//
//                    }
//                }
//        }
//
//
//        private fun uploadUserInfo(nickname: String, job: String, photoUrl: String){
//            val uid = FirebaseAuth.getInstance().uid ?: ""
//            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//
//            ref.child(column).setValue(info)
//                .addOnSuccessListener {
//                    Log.d("UserInfo", "info added: $info")
//                }
//                .addOnFailureListener{
//                    Log.d("UserInfo", "Failed to add info")
//                }
//        }


    }
}