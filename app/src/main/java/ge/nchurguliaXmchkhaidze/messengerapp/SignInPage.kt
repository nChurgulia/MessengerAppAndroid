package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignInPage : AppCompatActivity() {
    lateinit var  signUpButton : Button
    lateinit var  signInButton : Button
    lateinit var emailField: TextView
    lateinit var passwordField : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        setUpSignInButton()
        setUpSignUpButton()
        hideSoftKeyboard(R.id.nickname_si)
        hideSoftKeyboard(R.id.pass_si)
        emailField = findViewById<TextView>(R.id.nickname_si)
        passwordField = findViewById<TextView>(R.id.pass_si)
        signUpButton = findViewById<Button>(R.id.sign_up_button)
        signInButton = findViewById<Button>(R.id.sign_in_button)


    }


    private fun setUpSignUpButton(){
        findViewById<Button>(R.id.sign_up_button).setOnClickListener {
            goToSignUp()
        }
    }

    private fun setUpSignInButton(){
        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            var success = accountAccess.logIn(emailField.text.toString() + getString(R.string.mail_suffix),passwordField.text.toString())
            if(success){
                goToConversations()
            }
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