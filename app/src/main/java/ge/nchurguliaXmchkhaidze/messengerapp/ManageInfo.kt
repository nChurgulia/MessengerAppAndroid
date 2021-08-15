package ge.nchurguliaXmchkhaidze.messengerapp

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class ManageInfo {
    companion object {
        const val USERS = "users"
        const val NICKNAME = "nick"
        const val JOB = "job"
        const val PHOTO = "photo"

        fun uploadUserInformation(nick: String, job: String, imageUri: Uri?, imageURL: String, actionAfterLogged: (() -> Boolean)? , handleError: (String) -> Boolean ){
            if(imageUri == null) {
                uploadInfo(nick, job, imageURL, actionAfterLogged, handleError)
                return
            }
            Log.d("NULLCHECK", imageUri.toString())

            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(imageUri)
                    .addOnSuccessListener {
                        Log.d("Storage", "Succesfully uploaded image ${it.metadata?.path}")
                        ref.downloadUrl.addOnSuccessListener { currUrl ->
                            uploadInfo(nick, job, currUrl.toString(), actionAfterLogged, handleError)
                        }.addOnFailureListener{ currUrl ->
                            currUrl.message?.let { it1 -> handleError(it1) }
                        }
                    }.addOnFailureListener{
                        it.message?.let { it1 -> handleError(it1) }
                    }
        }

        fun getInfo(processInfo: (String, String, String) -> Boolean, handleError: (String) -> Boolean){
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            ref.get().addOnSuccessListener {
                val hMap = it.value as HashMap<Any,Any>
                val nickname = hMap["nick"]!! as String
                val job = hMap["job"]!! as String
                val photo = hMap["photo"]!! as String
                processInfo(nickname, job, photo)
            }
            .addOnFailureListener{
                it.message?.let { it1 -> handleError(it1) }
            }
        }

        private fun uploadInfo(nick: String, job: String, photoUrl: String, actionAfterLogged: (() -> Boolean)?, handleError: (String) -> Boolean){
             val uid = FirebaseAuth.getInstance().uid ?: ""
             val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
             val userDetails = HashMap<String, String> ()
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
                    it.message?.let { it1 -> handleError(it1) }
                }
        }
    }
}