package ge.nchurguliaXmchkhaidze.messengerapp

import com.google.firebase.auth.FirebaseAuth

class LastMessageManagement {
    companion object{

        fun getLastMessageInfo() {
            var uid = FirebaseAuth.getInstance().uid!!

        }

    }
}