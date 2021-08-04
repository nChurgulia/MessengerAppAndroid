package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SignUpPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        setUpSignUpButton()
        hideSoftKeyboard(R.id.nickname_su)
        hideSoftKeyboard(R.id.pass_su)
        hideSoftKeyboard(R.id.job_su)
    }

    private fun setUpSignUpButton(){
        findViewById<Button>(R.id.sign_up).setOnClickListener {
            val nick = findViewById<EditText>(R.id.nickname_su).text.toString()
            val pass = findViewById<EditText>(R.id.pass_su).text.toString()
            val job = findViewById<EditText>(R.id.job_su).text.toString()

            when {
                nick == "" -> showWarning(R.id.sign_up, getString(R.string.empty_nick))
                pass == "" -> showWarning(R.id.sign_up, getString(R.string.empty_pass))
                job == ""-> showWarning(R.id.sign_up, getString(R.string.empty_job))
                else -> goToConversations()
            }

            accountAccess.signUp(nick + getString(R.string.mail_suffix), pass, job, this::goToConversations)

        }
    }

    private fun goToConversations() : Boolean {
        val intent = Intent(this, ChatSearchPage::class.java)
        startActivity(intent)
        return false
    }

}