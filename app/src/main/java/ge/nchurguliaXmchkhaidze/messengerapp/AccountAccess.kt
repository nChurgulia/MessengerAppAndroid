package ge.nchurguliaXmchkhaidze.messengerapp

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import ge.nchurguliaXmchkhaidze.messengerapp.ManageInfo.Companion.uploadUserInformation

class AccountAccess {
    companion object {
        fun  signUp(email: String, password: String, job: String, actionAfterLogged: () -> Boolean)  {

            if(email.isEmpty() || password.isEmpty()) {
                //Toast.makeText(this, "One of the fields is empty!!!", Toast.LENGTH_SHORT).show()
                return
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    // if successful

                    Log.d(
                        "AuthenticationCheck",
                        "User created with credentials: ${it.result!!.user!!.uid} , $email , $password"
                    )
                    val imageUri: Uri =
                            Uri.parse("android.resource://ge.nchurguliaXmchkhaidze.messengerapp/drawable/avatar_image_placeholder")
                    uploadUserInformation(email.removeSuffix("@gmail.com"), job, imageUri, "", actionAfterLogged)

                }
                .addOnFailureListener{
                    Log.d("AuthenticationCheck", "Failed to create user: ${it.message}")
                   // Toast.makeText(this,  "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        fun logIn(email: String, password: String, actionAfterLogged: () -> Boolean) {
            // TODO:
            // check if email and password are empty!!!
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{


                    if(!it.isSuccessful) {
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

                    Log.d(
                        "AuthenticationCheck",
                        "Failed to logIn user: ${it.message} $email $password"
                    )
                    // Toast.makeText(this,  "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()

                }
        }

    }
}
