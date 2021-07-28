package ge.nchurguliaXmchkhaidze.messengerapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

fun Activity.hideSoftKeyboard(id: Int){
    findViewById<EditText>(id).onFocusChangeListener = View.OnFocusChangeListener { _, p1 ->
        if (!p1){
            (getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).
            hideSoftInputFromWindow((findViewById<EditText>(id)).windowToken, 0)
        }
    }
}