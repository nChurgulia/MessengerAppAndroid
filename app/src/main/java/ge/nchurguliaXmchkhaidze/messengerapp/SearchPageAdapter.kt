package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchPageAdapter (private val context : Context,  private val listListener: UserSearchInterface) : RecyclerView.Adapter<SearchPageAdapter.SearchPageVH>() {

    lateinit var items: ArrayList<ChatInfo>

    inner class SearchPageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.chat_info_name_view)
        private val lastMessage: TextView = itemView.findViewById(R.id.chat_info_last_message)
        private val timeAgo: TextView = itemView.findViewById(R.id.chat_info_time_ago)
        private val image: ImageView = itemView.findViewById(R.id.chat_info_image)


        fun bindChatInfo(username: String, lastMsg: String, time: String, profilePicture: String) {

            // Profile Name
            name.text = username
            // Last Message
            lastMessage.text = lastMsg
            // Time Ago
            timeAgo.text = time
            // Icon
            Glide.with(this.itemView.context).load(profilePicture).into(image)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPageVH {
        val linflater = LayoutInflater.from(context)
        val vw = linflater.inflate(R.layout.one_chat_info, parent,false)
        return SearchPageVH(vw)
    }

    override fun onBindViewHolder(holder: SearchPageVH, position: Int) {

        val chat = items[position]

        holder.itemView.setOnClickListener {
            listListener.goToChat(chat.name, chat.uid, chat.job, chat.profilePicture)
        }

        holder.bindChatInfo(chat.name, chat.lastMessage, chat.timeAgo, chat.profilePicture)
    }

    override fun getItemCount(): Int = items.size

}