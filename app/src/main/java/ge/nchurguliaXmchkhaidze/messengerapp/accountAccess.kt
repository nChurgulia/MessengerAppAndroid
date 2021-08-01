package ge.nchurguliaXmchkhaidze.messengerapp

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class accountAccess {
    companion object {
        fun  signUp( email : String, password: String) : Boolean {
            var success = false
            if(email.isEmpty() || password.isEmpty()) {
                //Toast.makeText(this, "One of the fields is empty!!!", Toast.LENGTH_SHORT).show()
                return success
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    // if successful

                    Log.d("Account","User created with credentials: ${ it.result!!.user!!.uid} , ${email} , ${password}")
                    success = true
                }
                .addOnFailureListener{
                    Log.d("Account","Failed to create user: ${it.message}")
                   // Toast.makeText(this,  "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            return success
        }

        fun logIn( email : String, password: String) : Boolean{
            var success = false
            // TODO:
            // check if email and password are empty!!!
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    // if successful
                    Log.d("Account","User Logged in with credentials:  , ${email} , ${password}")
                    success = true
                }
                .addOnFailureListener{
                    Log.d("Account","Failed to logIn user: ${it.message} ${email} ${password}")
                    // Toast.makeText(this,  "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            Log.d("Account","Success ${success} ")
            return success
        }

        }
    }
