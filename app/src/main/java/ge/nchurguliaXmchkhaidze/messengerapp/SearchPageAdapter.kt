package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchPageAdapter ( private val context : Context , private val items: MutableList< chatInfo >, private val listListener: UserSearchInterface) : RecyclerView.Adapter<SearchPageAdapter.SearchPageVH>() {

    inner class SearchPageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val name = itemView.findViewById<TextView>(R.id.chat_info_name_view)
        val lastMessage = itemView.findViewById<TextView>(R.id.chat_info_last_message)
        val timeAgo = itemView.findViewById<TextView>(R.id.chat_info_time_ago)
        val image = itemView.findViewById<ImageView>(R.id.chat_info_image)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPageVH {
        val linflater = LayoutInflater.from(context)
        val vw = linflater.inflate(R.layout.one_chat_info, parent,false)
        return SearchPageVH(vw)
    }

    override fun onBindViewHolder(holder: SearchPageVH, position: Int) {
        val chat = items[position]

        holder.itemView.setOnClickListener {
            listListener.goToChat((holder.itemView.findViewById(R.id.chat_info_name_view) as TextView).text.toString())
        }

        // Profile Name
        holder.name.text = chat.name
        // Last Message
        holder.lastMessage.text = chat.lastMessage
        // Time Ago
        holder.timeAgo.text = chat.timeAgo
        // Icon
        holder.image.setImageResource(chat.profilePicture)

    }

    override fun getItemCount(): Int = items.size


}