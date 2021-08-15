package ge.nchurguliaXmchkhaidze.messengerapp

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AccountAccess {
    companion object {
        fun  signUp(email: String, password: String, job: String, actionAfterLogged: () -> Boolean, handleError: (String) -> Boolean)  {

            if(email.isEmpty() || password.isEmpty()) {
                //Toast.makeText(this, "One of the fields is empty!!!", Toast.LENGTH_SHORT).show()
                return
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) {
                        it.exception?.message?.let { it1 -> handleError(it1) }
                        return@addOnCompleteListener
                    }

                    // if successful

                    Log.d(
                        "AuthenticationCheck",
                        "User created with credentials: ${it.result!!.user!!.uid} , ${email} , ${password}"
                    )
                    ManageInfo.uploadNick(email.removeSuffix("@gmail.com"), handleError)
                    ManageInfo.uploadJob(job, handleError)
                    val imageUri: Uri =
                        Uri.parse("android.resource://ge.nchurguliaXmchkhaidze.messengerapp/drawable/avatar_image_placeholder")
                    ManageInfo.uploadPhoto(imageUri, handleError)
                    //                    ManageInfo.uploadInfo("photo",
//                        "https://firebasestorage.googleapis.com/v0/b/messengerapp-4df98.appspot.com/o/images%2F3710caa5-f6a8-43d9-bb60-82ee6c122616?alt=media&token=4002d219-f93a-4094-90c0-34ef2cc6eff5")
                    actionAfterLogged()
                }
                .addOnFailureListener{
                    it.message?.let { it1 -> handleError(it1) }
                    Log.d("AuthenticationCheck", "Failed to create user: ${it.message}")
                   // Toast.makeText(this,  "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        fun logIn(email: String, password: String, actionAfterLogged: () -> Boolean, handleError: (String) -> Boolean) {

            // TODO:
            // check if email and password are empty!!!



            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{


                    if(!it.isSuccessful) {
                        it.exception?.message?.let { it1 -> handleError(it1) }
                        return@addOnCompleteListener
                    }

                    // if successful
                    Log.d(
                        "AuthenticationCheck",
                        "User Logged in with credentials:  , ${email} , ${password}"
                    )

                    actionAfterLogged()

                }
                .addOnFailureListener{
                    it.message?.let { it1 -> handleError(it1) }
                    Log.d(
                        "AuthenticationCheck",
                        "Failed to logIn user: ${it.message} ${email} ${password}"
                    )
                    // Toast.makeText(this,  "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()

                }
        }

    }
}
