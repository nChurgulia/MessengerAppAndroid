package ge.nchurguliaXmchkhaidze.messengerapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class ProfilePage : AppCompatActivity(), IErrorHandler {
    private var photoUri : Uri? = null

    private lateinit var nickView: TextView
    private lateinit var jobView: TextView
    private lateinit var photoView: ImageView
    private lateinit var updateButton: Button
    private lateinit var signoutButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        initViews()
        displayInfo()
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
            photoUri = data?.data!!
            findViewById<ImageView>(R.id.profile_pic).setImageURI(photoUri)
        }
    }

    private fun initViews(){
        nickView = findViewById(R.id.nickname_pr)
        jobView = findViewById(R.id.job_pr)
        photoView = findViewById(R.id.profile_pic)
        updateButton = findViewById(R.id.update)
        signoutButton = findViewById(R.id.sign_out)

    }

    private fun displayInfo(){

        startLoader()
        ManageInfo.getNick(this::displayNick, this::handleError)
        ManageInfo.getJob(this::displayJob, this::handleError)
        ManageInfo.getPhoto(this::displayPhoto, this::handleError)
    }

    private fun displayPhoto(url:String): Boolean{
        stopLoader()
        Glide.with(this).load(url).into(photoView)
        return true
    }

    private fun displayJob(job:String): Boolean{
        stopLoader()
        jobView.text = job
        return true
    }
    private fun displayNick(nick:String): Boolean{
        stopLoader()
        nickView.text = nick
        return true
    }

    private fun setUpUpdate(){
        updateButton.setOnClickListener {
            jobView.clearFocus()
            updateInfo()
        }
    }
    private fun setUpLogOut(){
        signoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            goToPage(this, SignInPage::class.java)
        }
    }

    private fun updateInfo() {
        if (jobView.text.toString() == "") {
            showWarning(getString(R.string.empty_job), jobView, findViewById(R.id.add_btn))
        }else{
            ManageInfo.uploadPhoto(photoUri, this::handleError)
            ManageInfo.uploadJob(jobView.text.toString(), this::handleError)
        }
    }

    private fun setUpImageView(){
        photoView.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, OPEN_GALLERY)
        }
    }




    private fun setUpNavBar(){
        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    goToPage(this, ChatSearchPage::class.java)
                    true
                }
                R.id.profile -> {
                    true
                }
                else -> false
            }
        }

        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            goToPage(this, UserSearchPage::class.java)
        }
    }

    override fun handleError(err: String): Boolean {
        showWarning(err, findViewById(R.id.update))
        return true
    }

    companion object {
        private const val OPEN_GALLERY = 1
    }
}