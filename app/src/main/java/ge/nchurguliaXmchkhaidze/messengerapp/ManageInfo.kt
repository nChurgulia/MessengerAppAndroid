package ge.nchurguliaXmchkhaidze.messengerapp

import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ManageInfo {
    companion object {
        fun uploadPhoto( imageUri : Uri?){
            if(imageUri == null) return
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(imageUri!!)
                    .addOnSuccessListener {
                        Log.d("Storage","Succesfully uploaded image ${it.metadata?.path}")
                        ref.downloadUrl.addOnSuccessListener {
                            Log.d("Storage", "Photo URL is: $it")
                            uploadInfo("photo",it.toString())
                        }
                    }
        }

        fun uploadJob(profession: String){
            uploadInfo("job",profession)
        }
        fun uploadNick(nickname: String){
            uploadInfo("nick",nickname)
        }



        fun getNick(processInfo: (String) -> Boolean)  {

            getInfo("nick",processInfo)
        }
        fun getJob(processInfo: (String) -> Boolean)  {
            getInfo("job",processInfo)
        }
        fun getPhoto(processInfo: (String) -> Boolean) {
            getInfo("photo",processInfo)
        }

        private fun getInfo(column: String, processInfo: (String) -> Boolean){
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

            ref.child(column).get()
                .addOnSuccessListener {
                    var value = ""
                    if(it.value != null){

                        value = it.value as String
                    }else{
                        Log.d("ReadInfo", "null $column")
                    }
                    Log.d("ReadInfo", "info read: $value")
                    processInfo(value)
                }
                .addOnFailureListener{
                    Log.d("ReadInfo", "Failed to read info")
                }
        }

         fun uploadInfo( column: String, info : String){
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

            ref.child(column).setValue(info)
                    .addOnSuccessListener {
                        Log.d("UserInfo", "info added: $info")
                    }
                    .addOnFailureListener{
                        Log.d("UserInfo", "Failed to add info")
                    }
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