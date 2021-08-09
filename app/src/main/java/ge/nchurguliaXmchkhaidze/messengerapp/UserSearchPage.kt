package ge.nchurguliaXmchkhaidze.messengerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class UserSearchPage : AppCompatActivity(), UserSearchInterface {
    private var adapter = UserSearchAdapter(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search_page)

        val recyclerView = findViewById<RecyclerView>(R.id.user_search_rv)
        recyclerView.adapter = adapter

        recyclerView.setOnTouchListener { v, event ->
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow(findViewById<EditText>(R.id.search_field).windowToken, 0)
            v?.onTouchEvent(event) ?: true
        }

        setUpSearch()
    }

    override fun onBackPressed() {
        goToPage(this, ChatSearchPage::class.java)
    }

    private fun updateUserList(uid: String?, nickname: String?, job: String?, photo: String?): Boolean {

        if (uid == null) {
            showWarning(R.id.search_field, getString(R.string.no_data))
        }else {
            adapter.list.add(UserInfo(nickname!!, job!!, photo!!))
            adapter.notifyDataSetChanged()
        }

        return true
    }

    override fun goToChat(user: String) {
        goToPage(this, ChatPage::class.java)
    }

    @SuppressLint("CheckResult")
    private fun setUpSearch() {
        val searchView = findViewById<EditText>(R.id.search_field)

        RxTextView.textChanges(searchView)
            .debounce(SEARCH_TIMEOUT, TimeUnit.MILLISECONDS)
            .filter { charSequence ->
                charSequence.trim().length >= SEARCH_LENGTH
            }
            .map { charSequence ->
                charSequence.toString().trim()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                adapter.list.clear()
                adapter.notifyDataSetChanged()
                ManageInfo.filterUsers(it, this::updateUserList)
            }
    }

    companion object {
        private const val SEARCH_LENGTH = 3
        private const val SEARCH_TIMEOUT: Long = 1000
    }
}