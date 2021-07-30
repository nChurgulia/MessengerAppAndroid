package ge.nchurguliaXmchkhaidze.messengerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.CollectionUtils.listOf
import java.util.*

class ConversationPageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENT) {
            ViewHolder1(
                LayoutInflater.from(parent.context).inflate(R.layout.sent_message, parent, false)
            )
        } else {
            ViewHolder2(
                LayoutInflater.from(parent.context).inflate(R.layout.received_message, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder1
        val msg = list[position]
        holder.bindMsg(msg)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return SENT
    }

    inner class ViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        fun bindMsg(msg: String) {
            txtName.text = msg
        }

        private val txtName = view.findViewById<TextView>(R.id.msg)
    }

    inner class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

    }

    companion object {
        private const val SENT = 1
        private const val RECEIVED = 2
    }
}