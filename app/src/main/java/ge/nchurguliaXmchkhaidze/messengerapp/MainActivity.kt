package ge.nchurguliaXmchkhaidze.messengerapp

import android.app.Activity
import android.content.Context
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

        setupKeyboardListener()
    }

    private fun setupKeyboardListener() {
        findViewById<EditText>(R.id.nickname).onFocusChangeListener = View.OnFocusChangeListener { _, p1 ->
            if (!p1){ hideSoftKeyboard(findViewById(R.id.nickname)) }
        }

        findViewById<EditText>(R.id.pass).onFocusChangeListener = View.OnFocusChangeListener { _, p1 ->
            if (!p1){ hideSoftKeyboard(findViewById(R.id.nickname)) }
        }

        findViewById<EditText>(R.id.job).onFocusChangeListener = View.OnFocusChangeListener { _, p1 ->
            if (!p1){ hideSoftKeyboard(findViewById(R.id.nickname)) }
        }
    }
}

fun Activity.hideSoftKeyboard(editText: EditText){
    (getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(editText.windowToken, 0)
}