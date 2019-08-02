package com.example.reservabuses.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.reservabuses.CredentialsManager
import com.example.reservabuses.R
import com.example.reservabuses.db.AppDatabase
import com.example.reservabuses.db.Reserve
import com.example.reservabuses.db.User
import kotlinx.android.synthetic.main.activity_buses_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.StringBuilder
import java.time.LocalDateTime


class BusesDetailsActivity : AppCompatActivity() {
    var currentUserId = 0
    private var currentUser: User? = null
    private var currentBusId: Int = 0

    override fun onCreate (savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        getUserId()
        setContentView(R.layout.activity_buses_details)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        setViewContent()
        setListeners()
        currentBusId = intent.getIntExtra("BUS_ID", 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun setViewContent () {
        reserveButton.text = resources.getString(R.string.reserve_button_text)
        Log.d("Bus ID:", currentBusId.toString())
        GlobalScope.launch(Dispatchers.IO){
            val dataBase = AppDatabase.getDatabase(baseContext)
            val reserveDao = dataBase.reserveDao()
            val userDao = dataBase.userDao()
            val busDao = dataBase.busDao()
            val currentBus = busDao.getBusById(currentBusId)
            val busIdString = currentBusId.toString()
            val sb = StringBuilder()
            sb.append(resources.getString(R.string.info_bus_details)).append(busIdString)
            val busIdTextString = sb.toString()
            busIdText.text = busIdTextString
            busScheduleText.text = currentBus?.schedule
            busCapacityText.text = busDao.getReserveCountForBus(currentBusId).toString() +" / " + resources.getInteger(R.integer.busCapacity).toString()
            if(reserveDao.getReserveByUserToday(currentUserId) != null){
                //reserve for today already exists, cannot make another
                reserveButton.isClickable = false
                reserveButton.setTextColor(resources.getColor(R.color.disabled_button_text))
                reserveButton.tooltipText = resources.getString(R.string.reserve_toast_text_success)
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setListeners (){
        reserveButton.setOnClickListener {
            val dataBase = AppDatabase.getDatabase(baseContext)
            val userDao = dataBase.userDao()
            val reserveDao = dataBase.reserveDao()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    reserveDao.insertAll(Reserve(0, currentUserId, currentBusId, LocalDateTime.now().toString()))
                    launch(Dispatchers.Main){
                        Toast.makeText(
                            baseContext,
                            resources.getString(R.string.reserve_toast_text_success),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Log.d("ERROR", "Error making reserve ${e.message}")
                    launch(Dispatchers.Main){
                        Toast.makeText(
                            baseContext,
                            resources.getString(R.string.reserve_toast_text_failure),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            super.onBackPressed()
        }
    }
    private fun getUserId(){
        GlobalScope.launch(Dispatchers.IO) {
            val dataBase = AppDatabase.getDatabase(baseContext)
            val userDao = dataBase.userDao()
            currentUser = userDao.getUser(CredentialsManager.getInstance(baseContext).loadUser()!!.first)
            currentUserId = currentUser!!.uid
        }
    }
}