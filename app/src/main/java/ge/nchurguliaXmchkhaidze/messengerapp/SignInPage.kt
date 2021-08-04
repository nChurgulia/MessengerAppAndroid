package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignInPage : AppCompatActivity() {
    private lateinit var  signUpButton : Button
    private lateinit var  signInButton : Button
    private lateinit var emailField: TextView
    private lateinit var passwordField : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        emailField = findViewById(R.id.nickname_si)
        passwordField = findViewById(R.id.pass_si)
        signUpButton = findViewById(R.id.sign_up_button)
        signInButton = findViewById(R.id.sign_in_button)

        setUpSignInButton()
        setUpSignUpButton()
        hideSoftKeyboard(R.id.nickname_si)
        hideSoftKeyboard(R.id.pass_si)


    }


    private fun setUpSignUpButton(){
        signUpButton.setOnClickListener {
            goToSignUp()
        }
    }

    private fun setUpSignInButton(){
        signInButton.setOnClickListener {
//            var success = accountAccess.logIn(emailField.text.toString() + getString(R.string.mail_suffix),passwordField.text.toString())
//            if(success){
                goToConversations()
//        }

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