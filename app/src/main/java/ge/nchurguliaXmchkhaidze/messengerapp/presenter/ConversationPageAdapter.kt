package ge.nchurguliaXmchkhaidze.messengerapp.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import ge.nchurguliaXmchkhaidze.messengerapp.R
import ge.nchurguliaXmchkhaidze.messengerapp.data.MessageInfo
import java.text.SimpleDateFormat
import java.util.*

class ConversationPageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: ArrayList<MessageInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENT) {
            SentMsgHolder(LayoutInflater.from(parent.context).inflate(R.layout.sent_message, parent, false))
        } else {
            ReceivedMsgHolder(LayoutInflater.from(parent.context).inflate(R.layout.received_message, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msgInfo = list[position]
        val currentDate = SimpleDateFormat("HH:mm").format(Date(msgInfo.sendTime))

        if (getItemViewType(position) == SENT) {
            holder as SentMsgHolder
            holder.bindMsg(msgInfo.content, currentDate)
        }else{
            holder as ReceivedMsgHolder
            holder.bindMsg(msgInfo.content, currentDate)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val uid = FirebaseAuth.getInstance().uid
        if (list[position].sender == uid){
            return SENT
        }
        return RECEIVED
    }

    inner class SentMsgHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindMsg(content: String, sendTime: String) {
            txtName.text = content
            time.text = sendTime
        }

        private val txtName = view.findViewById<TextView>(R.id.msg)
        private val time = view.findViewById<TextView>(R.id.sent_time)
    }

    inner class ReceivedMsgHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindMsg(content: String, sendTime: String) {
            txtName.text = content
            time.text = sendTime
        }

        private val txtName = view.findViewById<TextView>(R.id.reply)
        private val time = view.findViewById<TextView>(R.id.received_time)
    }

    companion object {
        private const val SENT = 1
        private const val RECEIVED = 2
    }
}