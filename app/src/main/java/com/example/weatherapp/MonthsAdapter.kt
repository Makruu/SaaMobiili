package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class MonthsAdapter(
        private val monthss: List<MonthsModel>
        ) : RecyclerView.Adapter<MonthsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.month_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val months2 = monthss[position]
        holder.observationMonth.text = months2.months
        holder.bind(months2)
    }

    override fun getItemCount() = monthss.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val observationMonth: Button = itemView.findViewById(R.id.observationMonth)


        fun bind(item: MonthsModel) {
            observationMonth.setOnClickListener{
                println("PERKELE " + item.months)
            }
        }
    }
}