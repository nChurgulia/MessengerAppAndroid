package ge.nchurguliaXmchkhaidze.messengerapp.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import ge.nchurguliaXmchkhaidze.messengerapp.R
import ge.nchurguliaXmchkhaidze.messengerapp.model.AccountAccess

class SignUpPage : AppCompatActivity(), IErrorHandler {
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
                nick == "" -> showWarning(getString(R.string.empty_nick), findViewById(R.id.sign_up))
                pass == "" -> showWarning(getString(R.string.empty_pass), findViewById(R.id.sign_up, ))
                job == ""-> showWarning(getString(R.string.empty_job), findViewById(R.id.sign_up, ))
                else -> AccountAccess.signUp(nick + getString(R.string.mail_suffix), pass, job, this::goToConversations, this::handleError)
            }
        }
    }

    private fun goToConversations() : Boolean {
        goToPage(this, ChatSearchPage::class.java)
        return false
    }

    override fun handleError(err: String): Boolean {
        val newErr = err.replace(getString(R.string.email_addr), getString(R.string.username), true)
        showWarning(newErr, findViewById<Button>(R.id.sign_up))
        return true
    }

}