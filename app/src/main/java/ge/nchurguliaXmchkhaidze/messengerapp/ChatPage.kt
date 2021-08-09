package ge.nchurguliaXmchkhaidze.messengerapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class ChatPage : AppCompatActivity() {
    private var list: ArrayList<MessageInfo> = ArrayList()
    private var adapter = ConversationPageAdapter()
    private lateinit var refSender: DatabaseReference
    private lateinit var refReceiver: DatabaseReference
    private lateinit var sender : String
    private lateinit var receiver: String
    private lateinit var user: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        initUsers()
        initReferences()
        setUpToolbar()
        setUpRV()
        loadChat()
        addMsgSentListener()
        hideSoftKeyboard(R.id.message_txt)
        scrollChat()
    }

    private fun loadChat(){
        ChatManagement.getConversation(sender,receiver,refSender,this::endLoadingChat)
    }

    private fun endLoadingChat(chat: ArrayList<MessageInfo>) : Boolean{
        list = chat

        var sortedChat = list.sortedWith(compareBy({it.sendTime}))
        list = ArrayList(sortedChat)
        adapter.list = list
        adapter.notifyDataSetChanged()
        return true
    }

    private fun initUsers(){
        sender = FirebaseAuth.getInstance().uid!!
        if(sender == "3un5nLkYCdhhV6pGSkOWgaQTgmI3"){
            receiver =  "INShS1zp54aqkBpIJtuxzLAZ0uU2"
        }else{
            receiver = "3un5nLkYCdhhV6pGSkOWgaQTgmI3"
        }

    }

    private fun initReferences(){
        refSender = FirebaseDatabase.getInstance().getReference("/messages/$sender/$receiver")
        refReceiver = FirebaseDatabase.getInstance().getReference("/messages/$receiver/$sender")
        refSender.addChildEventListener( object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(MessageInfo::class.java)
                if(chatMessage != null){
                    if(chatMessage.sender != sender){
                        Log.d("DataChanged", "Data got")
                        Log.d("DataChanged",chatMessage?.content!!)
                        list.add(chatMessage)
                        adapter.list = list
                        adapter.notifyDataSetChanged()
                    }

                }else{
                    Log.d("DataChanged", "Something Failed")
                }


            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        }

        )
    }

    override fun onBackPressed() {
        goToPage(this, ChatSearchPage::class.java)
    }

    private fun setUpToolbar(){

        findViewById<ImageButton>(R.id.back_button).setOnClickListener{
            goToPage(this, ChatSearchPage::class.java)
        }

        findViewById<AppBarLayout>(R.id.Search_Collapsing_Bar).addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val toolBarView = findViewById<LinearLayout>(R.id.tool_bar_view)
            val title = findViewById<TextView>(R.id.title)
            val subtitle = findViewById<TextView>(R.id.subtitle)
            when {
                (verticalOffset == 0) -> {
                    toolBarView.orientation = LinearLayout.VERTICAL
                    title.setPadding(EXPANDED_PADDING)
                    title.textSize = EXPANDED_TITLE_SIZE
                    subtitle.textSize = EXPANDED_SUBTITLE_SIZE
                } else -> {
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
        findViewById<TextInputLayout>(R.id.msg_field).setEndIconOnClickListener {
            val txt = findViewById<TextInputEditText>(R.id.message_txt).text.toString().trim()
            if(txt != "") {
                var msg = MessageInfo(sender, receiver, txt, Date().toString())
                list.add(msg)
                ChatManagement.sendMessage(msg,refSender,refReceiver)
               // list.add(MessageInfo("bla", "me", "vhkb???", Date()))
                adapter.list = list
                adapter.notifyDataSetChanged()
                findViewById<TextInputEditText>(R.id.message_txt).text = null
                scrollChat()
            }
        }
    }

    companion object {
        const val EXPANDED_TITLE_SIZE = 24.0F
        const val EXPANDED_SUBTITLE_SIZE = 14.0F
        const val EXPANDED_PADDING = 8
        const val COLLAPSED_TITLE_SIZE = 16.0F
        const val COLLAPSED_SUBTITLE_SIZE = 10.0F
        const val COLLAPSED_PADDING = 0
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