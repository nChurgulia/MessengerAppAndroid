package ge.nchurguliaXmchkhaidze.messengerapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatSearchPage : AppCompatActivity(), UserSearchInterface {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search_page)
        val chatPageRV = findViewById<RecyclerView>(R.id.SearchPageRV)
        var currList = getData()
        val secondPageA = SearchPageAdapter(this, currList, this)
        chatPageRV.adapter = secondPageA
        chatPageRV.setLayoutManager(LinearLayoutManager(this))
        chatPageRV.setOnTouchListener { v, event ->
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow((findViewById<androidx.appcompat.widget.SearchView>(R.id.search_field)).windowToken, 0)
            v?.onTouchEvent(event) ?: true
        }
        setUpNavBar()
    }

    override fun onStart() {
        super.onStart()
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).menu.getItem(0).isChecked = true
    }

    private fun getData(): MutableList<chatInfo> {
        var currList = mutableListOf<chatInfo>()

        for(i in 0..20){
            var curr = chatInfo("Afton Sixarulidze" ,  "On my way home but i needed to stop by the book store to..." + i.toString() , i.toString() + " min", R.drawable.avatar_image_placeholder)
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
                    goToProfile()
                    true
                }
                else -> false
            }
        }

        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            goToUserSearch()
        }
    }

    private fun goToProfile() {
        val intent = Intent(this, ProfilePage::class.java)
        startActivity(intent)
    }

    private fun goToUserSearch() {
        val intent = Intent(this, UserSearchPage::class.java)
        startActivity(intent)
    }

    override fun goToChat(user: String) {
        val intent = Intent(this, ChatPage::class.java)
        startActivity(intent)
    }
}