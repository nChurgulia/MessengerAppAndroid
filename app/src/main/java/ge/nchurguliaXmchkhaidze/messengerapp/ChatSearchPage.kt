package ge.nchurguliaXmchkhaidze.messengerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatSearchPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search_page)
        val chatPageRV = findViewById<RecyclerView>(R.id.SearchPageRV)
        var currList = getData()
        val secondPageA = SearchPageAdapter(this, currList)
        chatPageRV.adapter = secondPageA
        chatPageRV.setLayoutManager(LinearLayoutManager(this))
    }

    private fun getData(): MutableList<chatInfo> {
        var currList = mutableListOf<chatInfo>()

        for(i in 0..20){
            var curr = chatInfo("Afton Sixarulidze" ,  "Chemi Bjuturi Ra Mosatania " + i.toString() , i.toString() + " min", R.drawable.afton)
            currList.add(curr)
        }
        return  currList
    }


}