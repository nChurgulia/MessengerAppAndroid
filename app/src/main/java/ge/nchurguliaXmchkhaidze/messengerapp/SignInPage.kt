package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class SignInPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        hideSoftKeyboard(R.id.nickname_si)
        hideSoftKeyboard(R.id.pass_si)
    }

    fun goToSignUp(view: View) {
        val intent = Intent(this, SignUpPage::class.java)
        startActivity(intent)
    }
}