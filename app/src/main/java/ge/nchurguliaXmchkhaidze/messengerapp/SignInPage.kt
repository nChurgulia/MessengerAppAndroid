package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
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

        hideSoftKeyboard(R.id.nickname_si)
        hideSoftKeyboard(R.id.pass_si)
        emailField = findViewById<TextView>(R.id.nickname_si)
        passwordField = findViewById<TextView>(R.id.pass_si)
        signUpButton = findViewById<Button>(R.id.sign_up_button)
        signInButton = findViewById<Button>(R.id.sign_in_button)

        signUpButton.setOnClickListener{
            var success = accountAccess.signUp(emailField.text.toString(),passwordField.text.toString())
            if(success){
                goToSignUp()
            }
        }

        signInButton.setOnClickListener{
            var success = accountAccess.logIn(emailField.text.toString(),passwordField.text.toString())
            if(success){
                goToSignUp()
            }
        }
    }

    fun goToSignUp() {
        val intent = Intent(this, ChatSearchPage::class.java)
        startActivity(intent)
    }
}