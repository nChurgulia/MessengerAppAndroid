package ge.nchurguliaXmchkhaidze.messengerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LogInPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_page)

        hideSoftKeyboard(R.id.nickname_su)
        hideSoftKeyboard(R.id.pass_su)
        hideSoftKeyboard(R.id.job_su)
    }
}