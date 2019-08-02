package com.example.reservabuses.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.reservabuses.R
import com.example.reservabuses.db.Buses

class BusesAdapter(
    context: Context,
    private val dataSource: Array<Buses>
) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item_bus, parent, false)
        rowView.findViewById<TextView>(R.id.nameTextView).text = dataSource[position].bid.toString()
        rowView.findViewById<TextView>(R.id.dateTextView).text = dataSource[position].schedule.toString()
        rowView.findViewById<TextView>(R.id.capacityTextView).text =  "/" + dataSource[position].capacity.toString()
        return rowView
    }
}