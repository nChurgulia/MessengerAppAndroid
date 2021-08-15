package ge.nchurguliaXmchkhaidze.messengerapp.data

data class UserInfo(var nickname: String,
                    var job: String,
                    var photo: String,
                    var uid: String = ""){

    companion object {
        const val USERS = "users"
        const val NICK = "nick"
        const val JOB =  "job"
        const val PHOTO = "photo"
    }
}