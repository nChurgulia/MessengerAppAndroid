package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignInPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        setUpSignInButton()
        setUpSignUpButton()
        hideSoftKeyboard(R.id.nickname_si)
        hideSoftKeyboard(R.id.pass_si)
    }

    private fun setUpSignUpButton(){
        findViewById<Button>(R.id.sign_up).setOnClickListener {
            goToSignUp()
        }
    }

    private fun setUpSignInButton(){
        findViewById<Button>(R.id.sign_in).setOnClickListener {
            goToConversations()
        }
    }

    private fun goToSignUp() {
        val intent = Intent(this, SignUpPage::class.java)
        startActivity(intent)
    }

    private fun goToConversations() {
        val intent = Intent(this, ChatSearchPage::class.java)
        startActivity(intent)
    }
}