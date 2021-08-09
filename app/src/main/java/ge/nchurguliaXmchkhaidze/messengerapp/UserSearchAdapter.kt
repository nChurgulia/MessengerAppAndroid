package ge.nchurguliaXmchkhaidze.messengerapp

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.security.AccessController.getContext

class UserSearchAdapter(private val listListener: UserSearchInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list = mutableListOf<UserInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserInfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_info, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as UserInfoViewHolder
        holder.itemView.setOnClickListener {
            Log.d("job", list[position].photo)
            listListener.goToChat(list[position].nickname, list[position].uid, list[position].job, list[position].photo)
        }
        holder.bindUserInfo(list[position].nickname, list[position].job, list[position].photo)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class UserInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindUserInfo(nickname: String, job: String, photoUri: String) {
            userName.text = nickname
            userPosition.text = job
            Glide.with(this.itemView.context).load(photoUri).into(photo)
        }

        private val userName = view.findViewById<TextView>(R.id.nickname)
        private val userPosition = view.findViewById<TextView>(R.id.job)
        private val photo = view.findViewById<ImageView>(R.id.user_photo)
    }
}