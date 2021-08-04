package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        setUpUpdate()
        setUpLogOut()
        setUpImageView()
        setUpNavBar()
        hideSoftKeyboard(R.id.job_pr)
    }

    override fun onStart() {
        super.onStart()
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).menu.getItem(1).isChecked = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == OPEN_GALLERY) {
            findViewById<ImageView>(R.id.profile_pic).setImageURI(data?.data)
        }
    }

    private fun setUpUpdate(){
        findViewById<Button>(R.id.update).setOnClickListener {
            updateInfo()
        }
    }
    private fun setUpLogOut(){
        findViewById<Button>(R.id.sign_out).setOnClickListener {
            goToSignIn()
        }
    }

    private fun goToSignIn() {
        val intent = Intent(this, SignInPage::class.java)
        startActivity(intent)
    }

    private fun updateInfo() {

    }

    private fun setUpImageView(){
        findViewById<ImageView>(R.id.profile_pic).setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, OPEN_GALLERY)
        }
    }

    private fun setUpNavBar(){
        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    goToConversations()
                    true
                }
                R.id.profile -> {
                    true
                }
                else -> false
            }
        }

        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            goToUserSearch()
        }
    }

    private fun goToConversations() {
        val intent = Intent(this, ChatSearchPage::class.java)
        startActivity(intent)
    }

    private fun goToUserSearch() {
        val intent = Intent(this, UserSearchPage::class.java)
        startActivity(intent)
    }

    companion object {
        private const val OPEN_GALLERY = 1
    }
}