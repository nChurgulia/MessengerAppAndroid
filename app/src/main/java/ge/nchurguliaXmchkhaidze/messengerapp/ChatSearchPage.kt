package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatSearchPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search_page)
        val chatPageRV = findViewById<RecyclerView>(R.id.SearchPageRV)
        var currList = getData()
        val secondPageA = SearchPageAdapter(this, currList)
        chatPageRV.adapter = secondPageA
        chatPageRV.setLayoutManager(LinearLayoutManager(this))
        setUpNavBar()
    }

    private fun getData(): MutableList<chatInfo> {
        var currList = mutableListOf<chatInfo>()

        for(i in 0..20){
            var curr = chatInfo("Afton Sixarulidze" ,  "Chemi Bjuturi Ra Mosatania " + i.toString() , i.toString() + " min", R.drawable.afton)
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
}