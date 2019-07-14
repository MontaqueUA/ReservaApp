package com.example.reservabuses.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ReservaBuses.CredentialsManager
import com.example.reservabuses.R
import com.example.reservabuses.db.AppDatabase
import com.example.reservabuses.db.Buses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ReservaList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_complaints, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBusOptions()
        setListOnClickListener()
    }


    private fun loadBusOptions() {
        val busDao = AppDatabase.getDatabase(context!!).busDao()
        GlobalScope.launch(Dispatchers.IO) { // replaces doAsync (runs on another thread)
            val schedules: Buses? = busDao.getBusForToday()
            launch(Dispatchers.Main) {// replaces uiThread (runs on UIThread)
                val itemsAdapter = ComplaintAdapter(context!!, ArrayList(complaints))
                complaintsListView.adapter = itemsAdapter
            }
        }
    }

    private fun setListOnClickListener() {
        complaintsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedComplaint = (complaintsListView.adapter).getItem(position) as Complaint
            startActivity(
                Intent(context, ComplaintDetailsActivity::class.java).
                    putExtra("COMPLAINT_ID", selectedComplaint.id))
        }
    }
}