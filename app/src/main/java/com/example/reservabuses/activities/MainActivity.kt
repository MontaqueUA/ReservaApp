package com.example.reservabuses.activities
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reservabuses.R.string.action_bar_home_title
import com.example.reservabuses.db.AppDatabase
import com.example.reservabuses.db.Buses
import com.example.reservabuses.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import androidx.drawerlayout.widget.DrawerLayout
import com.example.reservabuses.CredentialsManager
import com.example.reservabuses.R
import com.example.reservabuses.RequestCode
import androidx.appcompat.widget.Toolbar
import com.example.reservabuses.R.integer
import com.example.reservabuses.db.Dao.BusDao
import androidx.core.view.GravityCompat
import com.example.reservabuses.fragments.ReserveFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val currentLoadedFragment: Fragment? = null
    private var drawer: DrawerLayout? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val busDao: BusDao = AppDatabase.getDatabase(baseContext).busDao()
        setContentView(R.layout.activity_main)
        setNavBar()
        GlobalScope.launch(Dispatchers.IO) {
            if(busDao.getBusForToday().isEmpty()){
                Log.d("Create schedules", "Today buses empty")
                createTodayBuses()
            }
        }
        loadUserDataOrSendToLoginActivity()
    }
    private fun loadUserDataOrSendToLoginActivity() {
        val userData = loadUserData()
        if (userData != null) {
            initializeHomeFragment()
        } else {
            goToLoginActivity()
        }
    }
    private fun setNavBar(){
        val toolbar: Toolbar = this.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navView: NavigationView = this.findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        this.drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener {menuItem ->
            menuItem.isChecked = true
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()

            when (menuItem.itemId){
                R.id.reserve -> {
                    val reserveFragment = supportFragmentManager.findFragmentByTag("reserveFragment")
                    if(reserveFragment != null){
                        transaction.replace(R.id.fragment_container, reserveFragment)
                    }else{
                        transaction.replace(R.id.fragment_container, ReserveFragment(), "reserveFragment")
                    }

                }
                else -> {
                    super.onOptionsItemSelected(menuItem)
                }
            }
            transaction.commit()
            true
        }
    }
    private fun loadUserData(): Pair<String, String>? {
        return CredentialsManager.getInstance(baseContext).loadUser()
    }

    private fun initializeHomeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        //transaction.replace(R.id.contentFrameLayout, HomeFragment(), "homeFrag")
        //transaction.commit()
        //navView.menu.getItem(0).isChecked = true
        this.supportActionBar!!.title = getString(action_bar_home_title)
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
            RequestCode.GO_TO_LOGIN_FROM_MAIN_ACTIVITY
        )
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
            val user : User? = AppDatabase.getDatabase(baseContext).userDao().getUser(userEmail)
            if (user == null) {
                AppDatabase.getDatabase(baseContext).userDao().insertAll(User(0, userEmail,userPassword))// Storing user,
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
        val schedules: IntArray = resources.getIntArray(R.array.schedules)
        val busesDao = AppDatabase.getDatabase(baseContext).busDao()
        val busCapacity: Int = resources.getInteger(R.integer.busCapacity)
        GlobalScope.launch(Dispatchers.IO) {
            for (schedule in schedules){
                val schedule_of_bus = todayDate.plusMinutes(schedule.toLong())
                busesDao.insertAll(Buses(0,schedule_of_bus.toString(),busCapacity))
            }
        }
    }
    override fun onBackPressed() {
        if (drawer?.isDrawerOpen(GravityCompat.START)!!) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
