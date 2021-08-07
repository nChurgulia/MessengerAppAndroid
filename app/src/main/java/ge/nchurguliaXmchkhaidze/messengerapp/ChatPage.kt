package ge.nchurguliaXmchkhaidze.messengerapp

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class ChatPage : AppCompatActivity() {
    private var list: ArrayList<MessageInfo> = ArrayList()
    private var adapter = ConversationPageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        setUpToolbar()
        setUpRV()
        addMsgSentListener()
        hideSoftKeyboard(R.id.message_txt)
        scrollChat()
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
                list.add(MessageInfo("me", "someone", txt, Date()))
                list.add(MessageInfo("bla", "me", "vhkb???", Date()))
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