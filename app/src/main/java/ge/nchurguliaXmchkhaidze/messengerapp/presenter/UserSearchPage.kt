package ge.nchurguliaXmchkhaidze.messengerapp.presenter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import ge.nchurguliaXmchkhaidze.messengerapp.R
import ge.nchurguliaXmchkhaidze.messengerapp.data.UserInfo
import ge.nchurguliaXmchkhaidze.messengerapp.model.UserSearchManagement
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class UserSearchPage : AppCompatActivity(), IUserSearch, IErrorHandler {
    private var adapter = UserSearchAdapter(this)
    private var userMng = UserSearchManagement()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search_page)


        hideSoftKeyboard(R.id.search_field)
        setUpSearch()
        setUpRV()
    }

    override fun onBackPressed() {
        goToPage(this, ChatSearchPage::class.java)
    }

     private fun updateUserList(uid: String?, nickname: String?, job: String?, photo: String?): Boolean {
         stopLoader()

         if (uid != null) {
             adapter.list.add(UserInfo(nickname!!, job!!, photo!!, uid))
             adapter.notifyDataSetChanged()
         } else if (adapter.list.isEmpty()) {
             showWarning(getString(R.string.no_data), findViewById(R.id.search_field))
         }

         return true
    }

    override fun goToChat(user: String, uid: String, job: String, photo: String) {
        val extras = mapOf(Pair(getString(R.string.chat_user), user),  Pair(getString(R.string.chatUserUid), uid), Pair(getString(R.string.chat_user_job), job), Pair(getString(R.string.chat_user_photo), photo))
        goToPage(this, ChatPage::class.java, extras)
    }

    @SuppressLint("CheckResult")
    private fun setUpSearch() {
        val searchView = findViewById<EditText>(R.id.search_field)

        RxTextView.textChanges(searchView)
            .debounce(SEARCH_TIMEOUT, TimeUnit.MILLISECONDS)
            .map { charSequence ->
                charSequence.toString().trim()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.length >= SEARCH_LENGTH){
                    findViewById<EditText>(R.id.search_field).clearFocus()
                    adapter.list.clear()
                    adapter.notifyDataSetChanged()
                    userMng.lastNode = ""
                    userMng.keyword = it
                    loadData()
                }else if (it.isEmpty()){
                    adapter.list.clear()
                    adapter.notifyDataSetChanged()
                    userMng.lastNode = ""
                    userMng.keyword = ""
                    loadData()
                }
            }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setUpRV(){

        val recyclerView = findViewById<RecyclerView>(R.id.user_search_rv)
        recyclerView.adapter = adapter

        recyclerView.setOnTouchListener { v, event ->
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow(findViewById<EditText>(R.id.search_field).windowToken, 0)
            v?.onTouchEvent(event) ?: true
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == adapter.list.size - 1) {
                    if(userMng.lastNode != "") {
                        loadData()
                    }
                }
            }
        })
    }

    fun loadData(){
        startLoader()
        userMng.lazyLoadUsers(this::updateUserList, this::handleError)
    }

    companion object {
        const val SEARCH_LENGTH = 3
        const val SEARCH_TIMEOUT: Long = 1000
    }

    override fun handleError(err: String): Boolean {
        showWarning(err, findViewById(R.id.search_field))
        return true
    }
}