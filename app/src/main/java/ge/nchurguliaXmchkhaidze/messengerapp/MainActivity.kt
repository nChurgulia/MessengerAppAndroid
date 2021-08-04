package ge.nchurguliaXmchkhaidze.messengerapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var intent : Intent
        if(FirebaseAuth.getInstance().currentUser == null){
            intent = Intent(this, SignInPage::class.java)
        }else{
            intent = Intent(this, ChatSearchPage::class.java)
        }
        startActivity(intent)
    }
}

fun Activity.hideSoftKeyboard(id: Int){
    findViewById<EditText>(id).onFocusChangeListener = View.OnFocusChangeListener { _, p1 ->
        if (!p1){
            (getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow((findViewById<EditText>(id)).windowToken, 0)
        }
        if (id == R.id.message_txt){
            scrollChat()
        }
    }
}

fun Activity.showWarning(id: Int, txt: String){
    val contextView = findViewById<View>(id)
    Snackbar.make(contextView, txt, Snackbar.LENGTH_SHORT).show()
}