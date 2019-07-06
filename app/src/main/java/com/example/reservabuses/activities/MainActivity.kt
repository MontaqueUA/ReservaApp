package com.example.reservabuses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private fun loadUserDataOrSendToLoginActivity() {
        val userData = loadUserData()
        if (userData != null) {
            initializeHomeFragment()
        } else {
            goToLoginActivity()
        }
    }
    private fun loadUserData(): Pair<String, String>? {
        return CredentialsManager.getInstance(baseContext).loadUser()
    }
    private fun initializeHomeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrameLayout, HomeFragment(), "homeFrag")
        transaction.commit()
        navView.menu.getItem(0).isChecked = true
        supportActionBar!!.title = getString(R.string.action_bar_home_title)
    }
    private fun goToLoginActivity() {
        // Clear current fragment to release memory.
        if (currentLoadedFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(currentLoadedFragment!!).commit()
        }
        // Initialize LogIn Activity
        startActivityForResult(
                Intent(this, LoginActivity::class.java),
                RequestCode.GO_TO_LOGIN_FROM_MAIN_ACTIVITY)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.GO_TO_LOGIN_FROM_MAIN_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        storeUserCredentials(data.extras!!)
                        initializeHomeFragment()
                    }
                }
            }
        }
    }
    private fun storeUserCredentials(bundle: Bundle) {
        val userEmail = bundle.getString("EMAIL")!!
        val userPassword = bundle.getString("PASSWORD")!!
        CredentialsManager.getInstance(baseContext).saveUser(userEmail, userPassword)
        GlobalScope.launch(Dispatchers.IO) {
            // Observation:
            // Since the user info is not coming from the API, (we are just storing it locally)
            // we have to previously check for it's existence so that we comply with UNIQUE
            // constraint "users.email"
            val user : User? = AppDatabase.getDatabase(baseContext).userDao().getUser(userEmail)
            if (user == null) {
                AppDatabase.getDatabase(baseContext).userDao().insertAll(User(userEmail))// Storing user,
                // this should come from an API
            }
        }
    }
}
