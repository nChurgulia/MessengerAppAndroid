package ge.nchurguliaXmchkhaidze.messengerapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserSearchAdapter(private val listListener: UserSearchInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list = mutableListOf<UserInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserInfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_info, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as UserInfoViewHolder
        holder.itemView.setOnClickListener {
            listListener.goToChat((holder.itemView.findViewById(R.id.nickname) as TextView).text.toString())
        }
        holder.bindUserInfo(list[position].nickname, list[position].job)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class UserInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindUserInfo(nickname: String, job: String) {
            userName.text = nickname
            userPosition.text = job
        }

        private val userName = view.findViewById<TextView>(R.id.nickname)
        private val userPosition = view.findViewById<TextView>(R.id.job)
        private val photo = view.findViewById<ImageView>(R.id.user_photo)
    }
}