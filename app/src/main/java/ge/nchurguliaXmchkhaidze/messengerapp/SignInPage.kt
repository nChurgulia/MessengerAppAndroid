package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class SignInPage : AppCompatActivity() {
    private lateinit var  signUpButton : Button
    private lateinit var  signInButton : Button
    private lateinit var emailField: TextView
    private lateinit var passwordField : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        initViews()

        setUpSignInButton()
        setUpSignUpButton()
        hideSoftKeyboard(R.id.nickname_si)
        hideSoftKeyboard(R.id.pass_si)
    }

    fun initViews(){
        emailField = findViewById(R.id.nickname_si)
        passwordField = findViewById(R.id.pass_si)
        signUpButton = findViewById(R.id.sign_up_button)
        signInButton = findViewById(R.id.sign_in_button)
    }


    private fun setUpSignUpButton(){
        signUpButton.setOnClickListener {
            goToSignUp()
        }
    }

    private fun setUpSignInButton(){
        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            accountAccess.logIn(emailField.text.toString() + getString(R.string.mail_suffix),passwordField.text.toString(), this::goToConversations ) }
    }

    private fun goToSignUp() {
        val intent = Intent(this, SignUpPage::class.java)
        startActivity(intent)
    }

    private fun goToConversations(): Boolean {
        val intent = Intent(this, ChatSearchPage::class.java)
        startActivity(intent)
        return true
    }


}