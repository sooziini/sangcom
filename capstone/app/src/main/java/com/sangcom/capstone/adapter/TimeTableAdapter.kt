package com.sangcom.capstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sangcom.capstone.R
import com.sangcom.capstone.dataclass.StuClass

class TimeTableAdapter(
    private val classList : ArrayList<StuClass>?,
    private val inflater: LayoutInflater
): RecyclerView.Adapter<TimeTableAdapter.StuClassViewHolder>() {

    inner class StuClassViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val classNum : TextView? = itemView.findViewById(R.id.timetable_item_class)
        private val className : TextView? = itemView.findViewById(R.id.timetable_item_classname)
        private val startTime : TextView? = itemView.findViewById(R.id.timetable_item_starttime)
        private val endTime : TextView? = itemView.findViewById(R.id.timetable_item_endtime)

        fun bind(stuClass: StuClass) {
            if (classList == null || classList.size == 0) {
                return
            }

            if (stuClass.period == null) {
                classNum?.text = ""
            } else {
                classNum?.text = stuClass.period.toString() + "교시"
            }

            className?.text = stuClass.subject
            startTime?.text = stuClass.startTime
            endTime?.text = stuClass.endTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuClassViewHolder {
        val view : View = if(classList == null || classList.size == 0) {
            inflater.inflate(R.layout.timetable_null_item, parent, false)
        } else {
            inflater.inflate(R.layout.timetable_item, parent, false)
        }
        return StuClassViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (classList == null || classList.size == 0)
            return 1

        return classList.size
    }

    override fun onBindViewHolder(holder: StuClassViewHolder, position: Int) {
        if (classList != null && classList?.size != 0) {
            holder.bind(classList[position])
        }
    }
}