package ge.nchurguliaXmchkhaidze.messengerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.tasks.await
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatSearchPage : AppCompatActivity(), UserSearchInterface {
    @SuppressLint("ClickableViewAccessibility")
    lateinit var currList: ArrayList<ChatInfo>
    lateinit var secondPageA: SearchPageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search_page)
        var chatPageRV = findViewById<RecyclerView>(R.id.SearchPageRV)
        currList = ArrayList<ChatInfo>()
        secondPageA = SearchPageAdapter(this,  this)
        secondPageA.items = currList
        chatPageRV.adapter = secondPageA
        chatPageRV.layoutManager = LinearLayoutManager(this)
        chatPageRV.setOnTouchListener { v, event ->
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow(findViewById<EditText>(R.id.search_field).windowToken, 0)
            v?.onTouchEvent(event) ?: true
        }
        setUpNavBar()
        LastMessageManagement.getLastMessageInfo(this::processData)

    }


    override fun onStart() {
        super.onStart()
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).menu.getItem(0).isChecked = true
    }

    private fun processData(data: ArrayList<LastMessageInfo>): Boolean{
        currList.clear()
        for(item in data){
            var fromId = item.fromId
            var content = item.content
            var sendTime = item.sendTime
            val date = formatTime(Date(sendTime))
            LastMessageManagement.loadUserData(fromId, content, date, this::loadOneUser)
        }

        return true
    }

    private fun loadOneUser(currData: ChatInfo): Boolean{
        currList.add(currData)
        secondPageA.items = currList
        secondPageA.notifyDataSetChanged()
        return true
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
        val extras = mapOf(Pair(getString(R.string.chat_user), user), Pair("uid", uid), Pair(getString(R.string.chat_user_job), job), Pair(getString(R.string.chat_user_photo), photo))
        goToPage(this, ChatPage::class.java, extras)
    }

    private fun formatTime(date: Date): String {

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