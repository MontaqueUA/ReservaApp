package com.example.reservabuses

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.ReservaBuses.CredentialsManager
import com.example.ReservaBuses.RequestCode
import com.example.reservabuses.Fragments.HomeFragment
import com.example.reservabuses.R.string
import com.example.reservabuses.R.integer
import com.example.reservabuses.activities.LoginActivity
import com.example.reservabuses.db.AppDatabase
import com.example.reservabuses.db.Buses
import com.example.reservabuses.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private val currentLoadedFragment: Fragment? = null
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
    /*private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
        setupNavViewListener()
    }*/
    private fun loadUserData(): Pair<String, String>? {
        return CredentialsManager.getInstance(baseContext).loadUser()
    }

    private fun initializeHomeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrameLayout, HomeFragment(), "homeFrag")
        transaction.commit()
        //navView.menu.getItem(0).isChecked = true
        supportActionBar!!.title = getString(string.action_bar_home_title)
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
    /*private fun setupNavViewListener() {
        navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            val transaction = supportFragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.home -> {
                    val homeFragment = supportFragmentManager.findFragmentByTag("homeFrag")
                    if (homeFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, homeFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, HomeFragment(), "homeFrag")
                    }
                    supportActionBar!!.title = getString(string.action_bar_home_title)
                }

                R.id.signOut -> {
                    onSignOut()
                }
            }

            transaction.commit()
            true
        }
    }*/
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
                AppDatabase.getDatabase(baseContext).userDao().insertAll(User(0,userEmail,userPassword))// Storing user,
                // this should come from an API
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createTodayBuses (){
        var todayDate: LocalDateTime = LocalDateTime.now()
        todayDate = todayDate.minusNanos(todayDate.nano.toLong())
        todayDate = todayDate.minusSeconds(todayDate.second.toLong())
        todayDate = todayDate.minusMinutes(todayDate.minute.toLong())
        todayDate = todayDate.minusHours(todayDate.hour.toLong())
        val schedules: Array<Int> = arrayOf(integer.morning715,
            integer.morning725,
            integer.morning735,
            integer.morning745_1,
            integer.morning745_2,
            integer.morning750_1,
            integer.morning750_2,
            integer.morning750_3,
            integer.morning755_1,
            integer.morning755_2,
            integer.morning800)
        val busesDao = AppDatabase.getDatabase(baseContext).busDao()
        GlobalScope.launch(Dispatchers.IO) {
            for (schedule in schedules){
                val schedule_of_bus = todayDate.plusMinutes(schedule.toLong())
                Log.d("Horas", "Horarios: " + schedule_of_bus.toString())
                busesDao.insertAll(Buses(0,schedule_of_bus,integer.busCapacity))
            }
        }


    }
}
