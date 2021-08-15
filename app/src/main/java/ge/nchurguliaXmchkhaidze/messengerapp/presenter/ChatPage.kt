package ge.nchurguliaXmchkhaidze.messengerapp.presenter

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ge.nchurguliaXmchkhaidze.messengerapp.data.MessageInfo
import ge.nchurguliaXmchkhaidze.messengerapp.model.ChatManagement
import java.util.*
import kotlin.collections.ArrayList
import ge.nchurguliaXmchkhaidze.messengerapp.R


class ChatPage : AppCompatActivity(), IErrorHandler {
    private var list: ArrayList<MessageInfo> = ArrayList()
    private var adapter = ConversationPageAdapter()
    private lateinit var refSender: DatabaseReference
    private lateinit var refReceiver: DatabaseReference
    private lateinit var sender : String
    private lateinit var receiver: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)
        setUpToolbar()
        initUsers()
        initReferences()
        setUpRV()
        loadChat()
        addMsgSentListener()
        hideSoftKeyboard(R.id.message_txt)
    }

    private fun loadChat(){
        startLoader()
        ChatManagement.getConversation(refSender, this::endLoadingChat, this::handleError)
    }

    private fun endLoadingChat(chat: ArrayList<MessageInfo>) : Boolean{
        stopLoader()

        if (chat.isEmpty()){
            showWarning(getString(R.string.new_conv), findViewById(R.id.messages))
        }else{
            list = chat
            val sortedChat = list.sortedWith(compareBy { Date(it.sendTime) })
            list = ArrayList(sortedChat)
            adapter.list = list
            adapter.notifyDataSetChanged()
            scrollChat()
        }
        return true
    }

    private fun initUsers(){
        sender = FirebaseAuth.getInstance().uid!!
    }

    private fun initReferences(){
        refSender = FirebaseDatabase.getInstance().getReference("/${MESSAGES}/$sender/$receiver")
        refReceiver = FirebaseDatabase.getInstance().getReference("/${MESSAGES}/$receiver/$sender")
        refSender.addChildEventListener( object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(MessageInfo::class.java)
                if(chatMessage != null){
                    if(chatMessage.sender != sender){
                        Log.d("DataChanged", chatMessage.content)
                        list.add(chatMessage)
                        adapter.list = list
                        adapter.notifyDataSetChanged()
                        scrollChat()
                    }
                }else{
                    Log.d("DataChanged", "Something Failed")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                handleError(error.message)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    override fun onBackPressed() {
        goToPage(this, ChatSearchPage::class.java)
    }

    private fun setUpToolbar(){

        val titleId = R.id.title
        val subTitleId = R.id.subtitle
        val extras = intent.extras
        if (extras != null) {
            findViewById<TextView>(titleId).text = extras.get(getString(R.string.chat_user)).toString()
            receiver = extras.get(UID).toString()
            findViewById<TextView>(subTitleId).text = extras.get(getString(R.string.chat_user_job)).toString()
            Glide.with(this).load(extras.get(getString(R.string.chat_user_photo)).toString()).into(findViewById(R.id. profile_pic))
        }

        findViewById<ImageButton>(R.id.back_button).setOnClickListener{
            goToPage(this, ChatSearchPage::class.java)
        }

        findViewById<AppBarLayout>(R.id.Search_Collapsing_Bar).addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val toolBarView = findViewById<LinearLayout>(R.id.tool_bar_view)
            val title = findViewById<TextView>(titleId)
            val subtitle = findViewById<TextView>(subTitleId)
            when {
                (verticalOffset == 0) -> {
                    toolBarView.orientation = LinearLayout.VERTICAL
                    title.setPadding(EXPANDED_PADDING)
                    title.textSize = EXPANDED_TITLE_SIZE
                    subtitle.textSize = EXPANDED_SUBTITLE_SIZE
                }
                else -> {
                    toolBarView.orientation = LinearLayout.HORIZONTAL
                    title.setPadding(COLLAPSED_PADDING)
                    title.textSize = COLLAPSED_TITLE_SIZE
                    subtitle.textSize = COLLAPSED_SUBTITLE_SIZE
                }
            }
        })

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
        val msgId = R.id.message_txt
        findViewById<TextInputLayout>(R.id.msg_field).setEndIconOnClickListener {
            val txt = findViewById<TextInputEditText>(msgId).text.toString().trim()
            if(txt != "") {
                val msg = MessageInfo(sender, receiver, txt, Date().toString())
                list.add(msg)
                ChatManagement.sendMessage(msg, refSender, refReceiver, this::handleError)
                adapter.list = list
                adapter.notifyDataSetChanged()
                findViewById<TextInputEditText>(msgId).text = null
                scrollChat()
            }
        }
    }

    override fun handleError(err: String): Boolean {
        showWarning(err, findViewById(R.id.message_txt))
        return true
    }

    companion object {
        const val EXPANDED_TITLE_SIZE = 24.0F
        const val EXPANDED_SUBTITLE_SIZE = 14.0F
        const val EXPANDED_PADDING = 8
        const val COLLAPSED_TITLE_SIZE = 16.0F
        const val COLLAPSED_SUBTITLE_SIZE = 10.0F
        const val COLLAPSED_PADDING = 0
        const val MESSAGES = "messages"
        const val UID = "uid"
    }

}

fun Activity.scrollChat(){
    val recyclerView = findViewById<RecyclerView>(R.id.messages)
    val position = (recyclerView.adapter as ConversationPageAdapter).itemCount - 1
    if (position >= 0) {
        findViewById<AppBarLayout>(R.id.Search_Collapsing_Bar).setExpanded(false)
        recyclerView.post { recyclerView.scrollToPosition(position) }
    }
}