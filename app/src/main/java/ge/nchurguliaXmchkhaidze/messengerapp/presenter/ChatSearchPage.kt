package ge.nchurguliaXmchkhaidze.messengerapp.presenter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.widget.RxTextView
import ge.nchurguliaXmchkhaidze.messengerapp.*
import ge.nchurguliaXmchkhaidze.messengerapp.data.ChatInfo
import ge.nchurguliaXmchkhaidze.messengerapp.data.LastMessageInfo
import ge.nchurguliaXmchkhaidze.messengerapp.model.LastMessageManagement
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatSearchPage : AppCompatActivity(), IUserSearch, IErrorHandler {
    private lateinit var currList: ArrayList<ChatInfo>
    private lateinit var secondPageA: SearchPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search_page)

        setUpRV()
        setUpSearch()
        setUpNavBar()
        hideSoftKeyboard(R.id.search_field)
        startLoader()
        LastMessageManagement.getLastMessageInfo(this::processData, this::handleError)
    }


    override fun onStart() {
        super.onStart()
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).menu.getItem(0).isChecked = true
    }

    private fun processData(data: ArrayList<LastMessageInfo>): Boolean{
        stopLoader()
        if (data.isEmpty()){
            showWarning(getString(R.string.no_data), findViewById(R.id.search_field), findViewById(R.id.add_btn))
        }else {
            currList.clear()
            for (item in data) {
                val fromId = item.fromId
                val content = item.content
                val sendTime = item.sendTime
                LastMessageManagement.loadUserData(fromId, content, sendTime, this::loadOneUser, this::handleError)
            }
        }
        return true
    }

    private fun loadOneUser(currData: ChatInfo): Boolean{
        currList.add(currData)
        currList.sortedWith(compareBy { Date(currData.timeAgo) }).reversed()
        secondPageA.items = currList
        secondPageA.notifyDataSetChanged()
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpRV(){
        currList = ArrayList()
        secondPageA = SearchPageAdapter(this,  this)
        secondPageA.items = currList
        val chatPageRV = findViewById<RecyclerView>(R.id.SearchPageRV)
        chatPageRV.adapter = secondPageA
        chatPageRV.layoutManager = LinearLayoutManager(this)
        chatPageRV.setOnTouchListener { v, event ->
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow(findViewById<EditText>(R.id.search_field).windowToken, 0)
            v?.onTouchEvent(event) ?: true
        }
    }

    private fun setUpNavBar(){
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.profile -> {
                    goToPage(this, ProfilePage::class.java)
                    true
                }
                else -> false
            }
        }

        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            goToPage(this, UserSearchPage::class.java)
        }
    }

    override fun goToChat(user: String, uid:String, job: String, photo: String) {
        val extras = mapOf(Pair(getString(R.string.chat_user), user), Pair(getString(R.string.chatUserUid), uid), Pair(getString(R.string.chat_user_job), job), Pair(getString(R.string.chat_user_photo), photo))
        goToPage(this, ChatPage::class.java, extras)
    }

    @SuppressLint("CheckResult")
    private fun setUpSearch() {
        val searchView = findViewById<EditText>(R.id.search_field)

        RxTextView.textChanges(searchView)
                .map { charSequence ->
                    charSequence.toString().trim()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    filterChats(it)
                }
    }

    private fun filterChats(prefix: String){
        val chats = ArrayList<ChatInfo>()

        for (i in currList) {
            if (i.name.startsWith(prefix)){
                chats.add(i)
            }
        }
        secondPageA.items = chats
        secondPageA.notifyDataSetChanged()
    }

    override fun handleError(err: String): Boolean {
        showWarning(err, findViewById(R.id.search_field))
        return true
    }

    companion object {
        fun formatTime(date: Date): String {

            val diff: Long = Date().time - date.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            return when {
                (days > 0) -> {
                    SimpleDateFormat("d MMM").format(date).toUpperCase()
                }
                (hours > 0) -> {
                    "$hours hour"
                }
                else -> {
                    "$minutes min"
                }
            }
        }
    }
}