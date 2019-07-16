package com.example.reservabuses.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reservabuses.R
import com.example.reservabuses.adapter.BusesAdapter
import com.example.reservabuses.db.AppDatabase
import com.example.reservabuses.db.Buses
import kotlinx.android.synthetic.main.fragment_reserve.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ReserveFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_reserve, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBusOptions()
        //setListOnClickListener()
    }

    @SuppressLint("SetTextI18n")
    private fun loadBusOptions() {
        val busDao = AppDatabase.getDatabase(context!!).busDao()
        GlobalScope.launch(Dispatchers.IO) {
            val schedules = busDao.getAllBuses()
            testBox.text = schedules[0].bid.toString() + schedules[0].schedule
            launch(Dispatchers.Main) {
                val itemsAdapter = BusesAdapter(context!!, schedules)
                busesListView.adapter = itemsAdapter
            }
        }
    }

    /*private fun setListOnClickListener() {
        busesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedBuses = (busesListView.adapter).getItem(position) as Buses
            startActivity(
                Intent(context, BusesDetailsActivity::class.java).
                    putExtra("COMPLAINT_ID", selectedBuses.id))
        }
    }*/
}