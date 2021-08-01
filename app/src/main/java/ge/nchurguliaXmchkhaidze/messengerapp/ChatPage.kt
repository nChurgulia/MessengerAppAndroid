package ge.nchurguliaXmchkhaidze.messengerapp

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class ChatPage : AppCompatActivity() {
    private var list: ArrayList<String> = ArrayList()
    private var adapter = ConversationPageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        setUpRV()
        addMsgSentListener()
        hideSoftKeyboard(R.id.message_txt)
        scrollChat()
    }

    private fun setUpRV(){
        val recyclerView = findViewById<RecyclerView>(R.id.messages)
        recyclerView.adapter = adapter

        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                scrollChat()
            }
        }
    }

    private fun addMsgSentListener(){
        findViewById<TextInputLayout>(R.id.msg_field).setEndIconOnClickListener {
            val txt = findViewById<TextInputEditText>(R.id.message_txt).text.toString().trim()
            if(txt != "") {
                list.add(txt)
                adapter.list = list
                adapter.notifyDataSetChanged()
                findViewById<TextInputEditText>(R.id.message_txt).text = null
                scrollChat()
            }
        }
    }
}

fun Activity.scrollChat(){
    val recyclerView = findViewById<RecyclerView>(R.id.messages)
    val position = (recyclerView.adapter as ConversationPageAdapter).itemCount - 1
    if (position >= 0) {
        recyclerView.post { recyclerView.scrollToPosition(position) }
    }
}