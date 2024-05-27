package com.szczepaniak.dawid.gymlog.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.models.ExerciseSet

class SetAdapter(private val sets: MutableList<ExerciseSet>, private val context: Context): RecyclerView.Adapter<SetAdapter.SetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_set_item, parent, false)
        return SetViewHolder(view)
    }

    override fun getItemCount(): Int {
       return sets.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {

        holder.tvSet.text = "${position + 1}"
        holder.tvPrevious.text = "-"
        holder.tvReps.text = "0"
        if((position + 1) % 2 == 0){
            holder.background.setBackgroundColor(com.google.android.material.R.attr.colorSurface)
        }
    }

    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvSet: TextView = itemView.findViewById(R.id.set_value)
        val tvPrevious: TextView = itemView.findViewById(R.id.previous_value)
        val tvReps: TextView = itemView.findViewById(R.id.reps_value)
        val background: View = itemView.findViewById(R.id.item_background)

    }

}