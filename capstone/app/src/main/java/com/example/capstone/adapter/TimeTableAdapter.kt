package com.example.capstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.dataclass.StuClass

class TimeTableAdapter (
    private val classList : ArrayList<StuClass>,
    private val inflater: LayoutInflater
): RecyclerView.Adapter<TimeTableAdapter.StuClassViewHolder>() {

    inner class StuClassViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val classNum : TextView = itemView.findViewById(R.id.timetable_item_class)
        private val className : TextView = itemView.findViewById(R.id.timetable_item_classname)
        private val startTime : TextView = itemView.findViewById(R.id.timetable_item_starttime)
        private val endTime : TextView = itemView.findViewById(R.id.timetable_item_endtime)

        fun bind(stuClass: StuClass, index : Int) {
            classNum.text = stuClass.classNum.toString()
            className.text = stuClass.className
            startTime.text = stuClass.startTime
            endTime.text = stuClass.endTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuClassViewHolder {
        val view = inflater.inflate(R.layout.timetable_item, parent, false)
        return StuClassViewHolder(view)
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    override fun onBindViewHolder(holder: StuClassViewHolder, position: Int) {
        holder.bind(classList[position], position)
    }
}