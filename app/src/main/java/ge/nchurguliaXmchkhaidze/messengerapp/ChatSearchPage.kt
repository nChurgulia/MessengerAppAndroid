package ge.nchurguliaXmchkhaidze.messengerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class ChatSearchPage : AppCompatActivity(), UserSearchInterface {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search_page)
        val chatPageRV = findViewById<RecyclerView>(R.id.SearchPageRV)
        val currList = getData()
        val secondPageA = SearchPageAdapter(this, currList, this)
        chatPageRV.adapter = secondPageA
        chatPageRV.layoutManager = LinearLayoutManager(this)
        chatPageRV.setOnTouchListener { v, event ->
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow(findViewById<EditText>(R.id.search_field).windowToken, 0)
            v?.onTouchEvent(event) ?: true
        }
        setUpNavBar()
    }

    override fun onStart() {
        super.onStart()
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).menu.getItem(0).isChecked = true
    }

    private fun getData(): MutableList<ChatInfo> {
        val currList = mutableListOf<ChatInfo>()

        for(i in 0..20){

            val date = formatTime(Date())

            val curr = ChatInfo("Afton Sixarulidze", "On my way home but i needed to stop by the book store to...$i", date, "https://www.thesprucepets.com/thmb/kV_cfc9P4QWe-klxZ8y--awxvY4=/960x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/adorable-white-pomeranian-puppy-spitz-921029690-5c8be25d46e0fb000172effe.jpg")
            currList.add(curr)
        }
        return  currList
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

    override fun goToChat(user: String, job: String, photo: String) {
        val extras = mapOf(Pair(getString(R.string.chat_user), user), Pair(getString(R.string.chat_user_job), job), Pair(getString(R.string.chat_user_photo), photo))
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