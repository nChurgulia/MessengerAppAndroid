package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView

class UserSearchPage : AppCompatActivity(), UserSearchInterface {
    private var adapter = UserSearchAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search_page)

        val recyclerView = findViewById<RecyclerView>(R.id.user_search_rv)
        recyclerView.adapter = adapter

        adapter.list = getData()
        adapter.notifyDataSetChanged()

        recyclerView.setOnTouchListener { v, event ->
            (getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow((findViewById<androidx.appcompat.widget.SearchView>(R.id.search_field)).windowToken, 0)

            v?.onTouchEvent(event) ?: true
        }
    }

    private fun getData(): MutableList<UserInfo> {
        val list = mutableListOf<UserInfo>()

        for(i in 0..5){
            list.add(UserInfo("Mariam Chkhaidze", "Queen", ""))
            list.add(UserInfo("123", "bla", ""))
        }
        return  list
    }

    override fun goToChat(user: String) {
        val intent = Intent(this, ChatPage::class.java)
        startActivity(intent)
    }
}