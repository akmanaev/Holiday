package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HolidayAdapter(private val holidays: List<Holiday>) :
    RecyclerView.Adapter<HolidayAdapter.HolidayViewHolder>() {

    class HolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_holiday, parent, false)
        return HolidayViewHolder(view)
    }

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        val holiday = holidays[position]
        holder.name.text = holiday.name
        holder.description.text = holiday.description
    }

    override fun getItemCount() = holidays.size
}
