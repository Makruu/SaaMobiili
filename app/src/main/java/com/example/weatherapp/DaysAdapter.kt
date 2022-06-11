package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class DaysAdapter (
    private val days: List<DaysModel>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.month_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val days2 = days[position]
        val paiva = days2.day.substring(8,10)
        val kuukausi = days2.day.substring(4,7)
        val klo = days2.day.substring(11,16)
        holder.observationMonth.text = paiva + kuukausi + " klo " + klo
    }

    override fun getItemCount() = days.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val observationMonth: Button = itemView.findViewById(R.id.observationMonth)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}