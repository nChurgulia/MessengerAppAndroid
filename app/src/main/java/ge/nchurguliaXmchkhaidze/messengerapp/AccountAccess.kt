package ge.nchurguliaXmchkhaidze.messengerapp

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import ge.nchurguliaXmchkhaidze.messengerapp.ManageInfo.Companion.uploadUserInformation

class AccountAccess {
    companion object {
        fun  signUp(email: String, password: String, job: String, actionAfterLogged: () -> Boolean, handleError: (String) -> Boolean)  {
            if(email.isEmpty() || password.isEmpty()) {
                return
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) {
                        it.exception?.message?.let { it1 -> handleError(it1) }
                        return@addOnCompleteListener
                    }
                    Log.d(
                        "AuthenticationCheck",
                        "User created with credentials: ${it.result!!.user!!.uid} , $email , $password"
                    )
                    val imageUri: Uri =
                            Uri.parse("android.resource://ge.nchurguliaXmchkhaidze.messengerapp/drawable/avatar_image_placeholder")
                    uploadUserInformation(email.removeSuffix("@gmail.com"), job, imageUri, "", actionAfterLogged, handleError)
                }
                .addOnFailureListener{
                    it.message?.let { it1 -> handleError(it1) }
                    Log.d("AuthenticationCheck", "Failed to create user: ${it.message}")
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
                        "User Logged in with credentials:  , $email , $password"
                    )
                    actionAfterLogged()
                }
                .addOnFailureListener{
                    it.message?.let { it1 -> handleError(it1) }
                    Log.d(
                        "AuthenticationCheck",
                        "Failed to logIn user: ${it.message} $email $password"
                    )
                }
        }
    }
}
